package org.auth1.auth1.core.authentication;

import org.auth1.auth1.core.Auth1;
import org.auth1.auth1.dao.TokenDao;
import org.auth1.auth1.dao.UserDao;
import org.auth1.auth1.model.entities.LoginToken;
import org.auth1.auth1.model.entities.User;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.auth1.auth1.core.authentication.AuthenticationStepResult.of;
import static org.auth1.auth1.core.authentication.AuthenticationStepResult.stepPassed;
import static org.auth1.auth1.model.Auth1Configuration.RequiredUserFields.*;

public class AuthenticationManager {
    private final Auth1 auth1;
    private final UserDao userDao;
    private final TokenDao tokenDao;
    private final List<AuthenticationStep> steps;

    public AuthenticationManager(Auth1 auth1, UserDao userDao, TokenDao tokenDao) {
        this.auth1 = auth1;
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.steps = StreamSupport.stream(STEPS.spliterator(), false)
                .map(f -> f.apply(this))
                .collect(Collectors.toList());
    }

    public RegistrationResult register(@Nullable  final String username, @Nullable final String email, final String rawPassword) {
        final var required = auth1.getAuth1Configuration().getRequiredUserFields();

        if((required == USERNAME_ONLY || required == USERNAME_AND_EMAIL) && username == null) {
            return RegistrationResult.USERNAME_REQUIRED;
        } else if((required == EMAIL_ONLY || required == USERNAME_AND_EMAIL) && email == null) {
            return RegistrationResult.EMAIL_REQUIRED;
        }

        final var password = auth1.getAuth1Configuration().getHashFunction().hash(rawPassword);
        final var newUser = new User(username, password, null, email, false, false, ZonedDateTime.now());
        userDao.register(newUser);
        return auth1.getAuth1Configuration().isEmailVerificationRequired()
                ? RegistrationResult.SUCCESS_CONFIRM_EMAIL : RegistrationResult.SUCCESS;
    }

    public AuthenticationResult authenticateByUsername(final String username, final String rawPassword, final @Nullable String totpCode) {
        return userDao
                .getUserByUsername(username)
                .map(user -> this.authenticate(user, rawPassword, totpCode))
                .orElseGet(() -> AuthenticationResult.forResult(AuthenticationResult.ResultType.USERNAME_DOES_NOT_EXIST));
    }

    public AuthenticationResult authenticateByEmail(final String email, final String rawPassword, final @Nullable String totpCode) {
        return userDao
                .getUserByEmail(email)
                .map(user -> this.authenticate(user, rawPassword, totpCode))
                .orElseGet(() -> AuthenticationResult.forResult(AuthenticationResult.ResultType.EMAIL_DOES_NOT_EXIST));
    }

    public AuthenticationResult authenticateByUsernameOrEmail(final String usernameOrEmail, final String password, final @Nullable String totpCode) {
        return userDao
                .getUserByUsername(usernameOrEmail)
                .or(() -> userDao.getUserByEmail(usernameOrEmail))
                .map(user -> this.authenticate(user, password, totpCode))
                .orElseGet(() -> AuthenticationResult.forResult(AuthenticationResult.ResultType.USERNAME_OR_EMAIL_DOES_NOT_EXIST));
    }

    private AuthenticationResult authenticate(final User user, final String password, final @Nullable String totpCode) {
        return steps.stream()
                .map(step -> step.doStep(user, password, totpCode))
                .filter(res -> !res.passed())
                .findFirst()
                .orElse(stepPassed())
                .getResult()
                .orElseGet(() -> {
                    final LoginToken token = LoginToken.withDuration(user.getId(), 23, TimeUnit.HOURS);
                    tokenDao.saveLoginToken(token);
                    return AuthenticationResult.forSuccess(new AuthenticationToken(token.getValue(), token.getExpirationTime()));
                });
    }

    private static Iterable<Function<AuthenticationManager, AuthenticationStep>> STEPS = Arrays.asList(
            mgr -> mgr::checkLocked,
            mgr -> mgr::checkRate,
            mgr -> mgr::checkVerified,
            mgr -> mgr::checkPassword,
            mgr -> mgr::checkTOTP);

    private AuthenticationStepResult checkLocked(User user, final String rawPassword, final @Nullable String totpCode) {
        return !user.isLocked() ? stepPassed() : of(AuthenticationResult.forResult(AuthenticationResult.ResultType.ACCOUNT_LOCKED));
    }

    private AuthenticationStepResult checkRate(User user, final String rawPassword, final @Nullable String totpCode) {
        return stepPassed(); // no rate limiting right now.
    }

    private AuthenticationStepResult checkVerified(User user, final String rawPassword, final @Nullable String totpCode) {
        final var notRequiredOrVerified = !auth1.getAuth1Configuration().isEmailVerificationRequired() || user.isVerified();
        return notRequiredOrVerified ? stepPassed() : of(AuthenticationResult.forResult(AuthenticationResult.ResultType.NOT_VERIFIED));
    }

    private AuthenticationStepResult checkPassword(User user, final String rawPassword, final @Nullable String totpCode) {
        final var match = this.auth1.getAuth1Configuration().getCheckFunction().check(rawPassword, user.getPassword());
        return match ? stepPassed() : of(AuthenticationResult.forResult(AuthenticationResult.ResultType.BAD_PASSWORD));
    }

    private AuthenticationStepResult checkTOTP(User user, final String rawPassword, final @Nullable String totpCode) {
        return stepPassed(); // no TOTP right now.
    }

}
