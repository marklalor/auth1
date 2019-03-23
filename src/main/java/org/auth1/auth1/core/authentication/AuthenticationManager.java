package org.auth1.auth1.core.authentication;

import static org.auth1.auth1.core.authentication.AuthenticationStepResult.of;
import static org.auth1.auth1.core.authentication.AuthenticationStepResult.stepPassed;
import static org.auth1.auth1.model.Auth1Configuration.RequiredUserFields.EMAIL_ONLY;
import static org.auth1.auth1.model.Auth1Configuration.RequiredUserFields.USERNAME_AND_EMAIL;
import static org.auth1.auth1.model.Auth1Configuration.RequiredUserFields.USERNAME_ONLY;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

import org.auth1.auth1.dao.TokenDao;
import org.auth1.auth1.dao.UserDao;
import org.auth1.auth1.model.Auth1Configuration;
import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.model.entities.UserAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * <p>The AuthenticationManager runs based on an {@link Auth1Configuration} and
 * the database access objects in order to perform the Auth1 logic functions.</p>
 *
 * <p>The manager has methods corresponding to all the HTTP endpoints, for example
 * {@link AuthenticationManager#register(String, String, String)}
 * or {@link AuthenticationManager#checkAuthenticationToken(String)}. These methods
 * are called by the HTTP code. They could also be easily called by some other
 * protocol if there is ever a need for another interface (e.g. protobuf).</p>
 */
@Component
public class AuthenticationManager {
    private final Auth1Configuration config;
    private final UserDao userDao;
    private final TokenDao tokenDao;
    private final List<AuthenticationStep> steps;

    public AuthenticationManager(Auth1Configuration config, UserDao userDao, TokenDao tokenDao) {
        this.config = config;
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.steps = StreamSupport.stream(STEPS.spliterator(), false)
                .map(f -> f.apply(this))
                .collect(Collectors.toList());
    }

    /**
     * Wrapper for inner functions that expect an unlocked {@link User} to perform
     * their functions. Will return the specified accountDoesNotExistResult
     * or accountIsLockedResult if the user specified by the userId
     * does not exist. Otherwise it will call the given function, passing in
     * the {@link User} that was received from the database.
     *
     * @param userId identifier of the user to perform the function with respect to
     * @param function function to call with the user, if it exists.
     * @param accountDoesNotExistResult result to return if the account specified by the userId does not exist
     * @param accountIsLockedResult result to return if the account specified by the userId is locked.
     */
    private <T> T performWithExistingAndUnlockedAccount(UserIdentifier userId, Function<User, T> function,
                                                        T accountDoesNotExistResult, T accountIsLockedResult) {
        return userId
                .getUser(userDao)
                .map(user -> {
                    if (user.isLocked()) {
                        return accountIsLockedResult;
                    } else {
                        return function.apply(user);
                    }
                })
                .orElse(accountDoesNotExistResult);
    }

    /**
     * <p>Generates and stores a password reset token for
     * the specified user. The token will be valid for 1 hour.</p>
     *
     * <p>Another method should still be used to communicate this
     * token to the user in some way. For example, by email.</p>
     *
     * @param userId identifier of the user to generate a password reset token for
     * @return a {@link GeneratePasswordResetTokenResult} which will include the token the method was successful.
     */
    public GeneratePasswordResetTokenResult generatePasswordResetToken(UserIdentifier userId) {
        return performWithExistingAndUnlockedAccount(
                userId,
                this::generatePasswordResetToken,
                GeneratePasswordResetTokenResult.ACCOUNT_DOES_NOT_EXIST,
                GeneratePasswordResetTokenResult.ACCOUNT_LOCKED
        );
    }

    private GeneratePasswordResetTokenResult generatePasswordResetToken(User user) {
        final PasswordResetToken token = PasswordResetToken.withDuration(user.getId(), 1, TimeUnit.HOURS);
        tokenDao.savePasswordResetToken(token);
        return GeneratePasswordResetTokenResult
                .forSuccess(new ExpiringToken(token.getValue(), token.getExpirationTime()));
    }

    /**
     * <p>Resets the password of the account associated with the given
     * password reset token. The token authorizes the password reset
     * as well as identifies the user whose password is to be reset.</p>
     *
     *
     * @param passwordResetToken the string of the {@link PasswordResetToken} to use to reset the password.
     * @param newPassword the new password to replace the old one with.
     * @return a {@link ResetPasswordResult} which describes how the operation failed or succeeded.
     */
    public ResetPasswordResult resetPassword(String passwordResetToken, String newPassword) {
        return tokenDao.getPasswordResetToken(passwordResetToken)
                .map(PasswordResetToken::getUserId)
                .flatMap(userDao::getUserById)
                .map(user -> {
                    if (this.passwordConformsToRules(newPassword)) {
                        user.setPassword(this.config.getHashFunction().hash(newPassword));
                        userDao.saveUser(user);
                        return ResetPasswordResult.SUCCESS;
                    } else {
                        return ResetPasswordResult.INVALID_PASSWORD;
                    }
                }).orElse(ResetPasswordResult.INVALID_TOKEN);
    }

    /**
     * <p>Determines whether the given password is valid depending on the
     * rules specified by the user.</p>
     * @param password the password to check the conformity of.
     * @return <code>True</code> if the password passes all
     * checks, <code>False</code> if it fails any of them.
     */
    private boolean passwordConformsToRules(String password) {
        return true; // TODO: password strength policy
    }

    /**
     * <p>Checks the validity of the given user authentication token.</p>
     *
     * @param token the string of the {@link UserAuthenticationToken} to check.
     * @return a {@link CheckAuthenticationTokenResult} that describes the validity
     * of the token, and includes the associated user id if it is valid.
     */
    public CheckAuthenticationTokenResult checkAuthenticationToken(String token) {
        return tokenDao
                .getAuthToken(token)
                .map(UserAuthenticationToken::getUserId)
                .map(CheckAuthenticationTokenResult::forSuccess)
                .orElseGet(CheckAuthenticationTokenResult::forInvalid);
    }

    /**
     * <p>Attempts to create a new user specified by the given parameters.
     * One of the username or email may be null if they
     * are not required by the {@link Auth1Configuration}.</p>
     *
     * @param username the username of the new user to be created.
     * @param email the email of the new user to be created.
     * @param rawPassword the raw (not hashed) password of the new user.
     *                    This will be checked for validity so that weak passwords are not possible.
     * @return a {@link RegistrationResult} that describes the outcome of the operation.
     */
    public RegistrationResult register(@Nullable final String username, @Nullable final String email, final String rawPassword) {
        final var required = this.config.getRequiredUserFields();

        if ((required == USERNAME_ONLY || required == USERNAME_AND_EMAIL) && username == null) {
            return RegistrationResult.USERNAME_REQUIRED;
        } else if ((required == EMAIL_ONLY || required == USERNAME_AND_EMAIL) && email == null) {
            return RegistrationResult.EMAIL_REQUIRED;
        }

        final var password = this.config.getHashFunction().hash(rawPassword);
        final var newUser = new User(username, password, null, email, false, false, ZonedDateTime.now());
        userDao.saveUser(newUser);
        return this.config.isEmailVerificationRequired()
                ? RegistrationResult.SUCCESS_CONFIRM_EMAIL : RegistrationResult.SUCCESS;
    }

    /**
     * <p>Authenticates based on the provided userId and rawPassword (and totpCode
     * if 2FA is configured). If the user and password match, then the
     * returned {@link AuthenticationResult} will contain a user authentication token.</p>
     *
     * @param userId an identifier of the user account to attempt to authenticate against.
     * @param rawPassword the password (unhashed) associated with the account to authenticate against.
     * @param totpCode the current TOTP passcode of the associated account.
     *                 Should be null if TOTP is not configured for this account.
     * @return an {@link AuthenticationResult} that encapsulates the outcome of the operation.
     */
    public AuthenticationResult authenticate(final UserIdentifier userId, final String rawPassword, final @Nullable String totpCode) {
        return performWithExistingAndUnlockedAccount(userId,
                user -> this.authenticate(user, rawPassword, totpCode),
                AuthenticationResult.USER_DOES_NOT_EXIST,
                AuthenticationResult.ACCOUNT_LOCKED);
    }

    private AuthenticationResult authenticate(final User user, final String rawPassword, final @Nullable String totpCode) {
        return steps.stream()
                .map(step -> step.doStep(user, rawPassword, totpCode))
                .filter(AuthenticationStepResult::failed)
                .findFirst()
                .flatMap(AuthenticationStepResult::getResult)
                .orElseGet(() -> {
                    final UserAuthenticationToken token = UserAuthenticationToken.withDuration(user.getId(), 23, TimeUnit.HOURS);
                    tokenDao.saveLoginToken(token);
                    return AuthenticationResult.forSuccess(new ExpiringToken(token.getValue(), token.getExpirationTime()));
                });
    }

    private static Iterable<Function<AuthenticationManager, AuthenticationStep>> STEPS = Arrays.asList(
            mgr -> mgr::checkLocked,
            mgr -> mgr::checkRate,
            mgr -> mgr::checkVerified,
            mgr -> mgr::checkPassword,
            mgr -> mgr::checkTOTP);

    private AuthenticationStepResult checkLocked(User user, final String rawPassword, final @Nullable String totpCode) {
        return !user.isLocked() ? stepPassed() : of(AuthenticationResult.ACCOUNT_LOCKED);
    }

    private AuthenticationStepResult checkRate(User user, final String rawPassword, final @Nullable String totpCode) {
        return stepPassed(); // no rate limiting right now.
    }

    private AuthenticationStepResult checkVerified(User user, final String rawPassword, final @Nullable String totpCode) {
        final var notRequiredOrVerified = !this.config.isEmailVerificationRequired() || user.isVerified();
        return notRequiredOrVerified ? stepPassed() : of(AuthenticationResult.NOT_VERIFIED);
    }

    private AuthenticationStepResult checkPassword(User user, final String rawPassword, final @Nullable String totpCode) {
        final var match = this.config.getCheckFunction().check(rawPassword, user.getPassword());
        return match ? stepPassed() : of(AuthenticationResult.BAD_PASSWORD);
    }

    private AuthenticationStepResult checkTOTP(User user, final String rawPassword, final @Nullable String totpCode) {
        return stepPassed(); // no TOTP right now.
    }

}
