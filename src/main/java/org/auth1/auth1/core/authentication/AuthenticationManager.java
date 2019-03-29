package org.auth1.auth1.core.authentication;

import static org.auth1.auth1.core.authentication.AuthenticationStepResult.of;
import static org.auth1.auth1.core.authentication.AuthenticationStepResult.stepPassed;
import static org.auth1.auth1.model.Auth1Configuration.RequiredUserFields.EMAIL_ONLY;
import static org.auth1.auth1.model.Auth1Configuration.RequiredUserFields.USERNAME_AND_EMAIL;
import static org.auth1.auth1.model.Auth1Configuration.RequiredUserFields.USERNAME_ONLY;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

import org.auth1.auth1.dao.TentativeTOTPConfigurationDao;
import org.auth1.auth1.dao.TokenDao;
import org.auth1.auth1.dao.UserDao;
import org.auth1.auth1.model.Auth1Configuration;
import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.TentativeTOTPConfiguration;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.model.entities.UserAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);

    private final Auth1Configuration config;
    private final UserDao userDao;
    private final TokenDao tokenDao;
    private final TentativeTOTPConfigurationDao tentativeTOTPConfigurationDao;

    private final List<AuthenticationStep> steps;

    public AuthenticationManager(Auth1Configuration config, UserDao userDao, TokenDao tokenDao, TentativeTOTPConfigurationDao tentativeTOTPConfigurationDao) {
        this.config = config;
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.tentativeTOTPConfigurationDao = tentativeTOTPConfigurationDao;
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
        logger.debug("Finding account for: " + userId);
        return userId
                .getUser(userDao)
                .map(user -> {
                    if (user.isLocked()) {
                        logger.debug("Account locked for " + userId);
                        return accountIsLockedResult;
                    } else {
                        logger.debug("Found account for " + userId + ". Executing function.");
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
        logger.debug("Generating a password reset token for " + user.getAnyUserIdentifier());
        final PasswordResetToken token = PasswordResetToken.withDuration(user.getId(), 1, TimeUnit.HOURS);
        tokenDao.savePasswordResetToken(token);
        logger.debug("Saved password reset token for " + user.getAnyUserIdentifier());
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
        logger.debug("Resetting password with token");
        return tokenDao.getPasswordResetToken(passwordResetToken)
                .map(PasswordResetToken::getUserId)
                .flatMap(userDao::getUserById)
                .map(user -> {
                    if (this.passwordConformsToRules(newPassword)) {
                        user.setPassword(this.config.getHashFunction().hash(newPassword));
                        userDao.saveUser(user);
                        logger.debug("Successfully reset password for " + user.getAnyUserIdentifier());
                        return ResetPasswordResult.SUCCESS;
                    } else {
                        logger.debug("Password reset candidate did not conform to password rules.");
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
        logger.debug("Checking authentication token \"" + token.substring(0, 3) + "...\"");
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
        logger.debug("Registering new user...");
        final var required = this.config.getRequiredUserFields();

        if ((required == USERNAME_ONLY || required == USERNAME_AND_EMAIL) && username == null) {
            return RegistrationResult.USERNAME_REQUIRED;
        } else if ((required == EMAIL_ONLY || required == USERNAME_AND_EMAIL) && email == null) {
            return RegistrationResult.EMAIL_REQUIRED;
        }

        final var password = this.config.getHashFunction().hash(rawPassword);
        final var newUser = new User(username, password, null, email, false, false, ZonedDateTime.now());
        userDao.saveUser(newUser);
        logger.debug("Created new user: (username=\"" + username + "\", email=\"" + email + "\"");
        return this.config.isEmailVerificationRequired()
                ? RegistrationResult.SUCCESS_CONFIRM_EMAIL : RegistrationResult.SUCCESS;
    }

    private CreateTentativeTOTPResult createTentativeTOTPSecret(int userId) {
        logger.debug("Saving new tentative TOTP configuration for user " + userId);
        TentativeTOTPConfiguration config = TentativeTOTPConfiguration.forUser(userId);
        tentativeTOTPConfigurationDao.saveConfiguration(config);
        return CreateTentativeTOTPResult.forSuccess(config.getTentativeTOTPSecret(), config.getExpirationTime());
    }

    public CreateTentativeTOTPResult createTentativeTOTPSecret(String token) {
        return tokenDao.getAuthToken(token)
                .map(UserAuthenticationToken::getUserId)
                .map(this::createTentativeTOTPSecret)
                .orElse(CreateTentativeTOTPResult.INVALID_TOKEN);
    }

    private ConfirmTentativeTOTPResult confirmTentativeTOTPSecret(int userId, String code) {
        return tentativeTOTPConfigurationDao
                .getConfiguration(userId)
                .map(config -> {
                    if (config.getExpirationTime().isBefore(ZonedDateTime.now())) {
                        return ConfirmTentativeTOTPResult.TENTATIVE_TOTP_SECRET_EXPIRED;
                    } else {
                        final boolean codeValid = checkTOTP(config.getTentativeTOTPSecret(), code);
                        if (codeValid) {
                            Optional<User> userById = userDao.getUserById(userId);
                            userById.map(user -> {
                                user.setTotpSecret(config.getTentativeTOTPSecret());
                                userDao.saveUser(user);
                                return ConfirmTentativeTOTPResult.SUCCESS;
                            }).orElse(ConfirmTentativeTOTPResult.INVALID_TOKEN);
                            return ConfirmTentativeTOTPResult.SUCCESS;
                        } else {
                            return ConfirmTentativeTOTPResult.INVALID_CODE;
                        }
                    }
                }).orElse(ConfirmTentativeTOTPResult.TENTATIVE_TOTP_SECRET_NOT_CREATED);
    }

    public ConfirmTentativeTOTPResult confirmTentativeTOTPSecret(String token, String code) {
        return tokenDao.getAuthToken(token)
                .map(UserAuthenticationToken::getUserId)
                .map(userId -> confirmTentativeTOTPSecret(userId, code))
                .orElse(ConfirmTentativeTOTPResult.INVALID_TOKEN);
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
        logger.debug("Authenticating: " + userId);
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
        logger.debug("Authentication step: checking if user is locked. " + user.getAnyUserIdentifier());
        return !user.isLocked() ? stepPassed() : of(AuthenticationResult.ACCOUNT_LOCKED);
    }

    private AuthenticationStepResult checkRate(User user, final String rawPassword, final @Nullable String totpCode) {
        logger.debug("Authentication step: checking if account is rate-limited. " + user.getAnyUserIdentifier());
        return stepPassed(); // no rate limiting right now.
    }

    private AuthenticationStepResult checkVerified(User user, final String rawPassword, final @Nullable String totpCode) {
        logger.debug("Authentication step: checking if user is verified. " + user.getAnyUserIdentifier());
        final var notRequiredOrVerified = !this.config.isEmailVerificationRequired() || user.isVerified();
        return notRequiredOrVerified ? stepPassed() : of(AuthenticationResult.NOT_VERIFIED);
    }

    private AuthenticationStepResult checkPassword(User user, final String rawPassword, final @Nullable String totpCode) {
        logger.debug("Authentication step: checking user password. " + user.getAnyUserIdentifier());
        final var match = this.config.getCheckFunction().check(rawPassword, user.getPassword());
        return match ? stepPassed() : of(AuthenticationResult.BAD_PASSWORD);
    }

    // TOTP constants are set because Google Authenticator hardcodes these!
    public final static long TOTP_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(30);
    public final static String TOTP_HMAC_ALGORITHM = "HmacSHA1";

    /**
     * Periods relative to the current period that are still considered valid. For example {0, -1} considers the
     * currently-valid TOTP code, as well as the code that was valid 30 seconds ago. This is very common because
     * often people are typing in a passcode when the time period changes. Especially important for accessibility
     * purposes in case it takes someone a long time to type their TOTP code.
     */
    public final static long[] VALID_TOTP_PERIODS = {0, -1};


    private static boolean checkTOTP(final byte[] secret, final String code) {
        final var currentPeriods = System.currentTimeMillis() / TOTP_PERIOD_MILLIS;
        return Arrays.stream(VALID_TOTP_PERIODS)
                .map(offset -> currentPeriods + offset)
                .mapToObj(time -> TOTP.generateTOTP(secret, Long.toHexString(time).toUpperCase(), 6, TOTP_HMAC_ALGORITHM))
                .anyMatch(code::equalsIgnoreCase);
    }

    private AuthenticationStepResult checkTOTP(User user, final String rawPassword, final @Nullable String totpCode) {
        logger.debug("Authentication step: checking TOTP code. " + user.getAnyUserIdentifier());
        return Optional.ofNullable(user.getTotpSecret())
                .map(secret -> checkTOTP(secret, totpCode))
                .map(result -> result ? stepPassed() : of(AuthenticationResult.BAD_TOTP))
                .orElse(stepPassed());
    }

}
