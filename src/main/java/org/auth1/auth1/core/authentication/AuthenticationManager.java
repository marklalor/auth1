package org.auth1.auth1.core.authentication;

import org.auth1.auth1.core.Auth1;
import org.auth1.auth1.dao.TokenDaoImpl;
import org.auth1.auth1.dao.UserDao;
import org.auth1.auth1.dao.UserDaoImpl;
import org.auth1.auth1.model.entities.LoginToken;
import org.auth1.auth1.model.entities.User;
import org.hibernate.cfg.NotYetImplementedException;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AuthenticationManager {
    private final Auth1 auth1;
    private final List<AuthenticationStep> steps;

    public AuthenticationManager(Auth1 auth1) {
        this.auth1 = auth1;
        this.steps = StreamSupport.stream(STEPS.spliterator(), false)
                .map(f -> f.apply(this))
                .collect(Collectors.toList());
    }

    public RegistrationResult register(final String username, final String email, final String password) {
        throw new NotYetImplementedException();
    }

    public AuthenticationResult authenticateByUsername(final String username, final String password, final @Nullable String totpCode) {
        return new UserDaoImpl(this.auth1.getDatabaseManager())
                .getUserByUsername(username)
                .map(user -> this.authenticate(user, password, totpCode))
                .orElse(AuthenticationResult.forResult(AuthenticationResult.ResultType.BAD_USERNAME));
    }

    public AuthenticationResult authenticateByEmail(final String email, final String password, final @Nullable String totpCode) {
        return new UserDaoImpl(this.auth1.getDatabaseManager())
                .getUserByEmail(email)
                .map(user -> this.authenticate(user, password, totpCode))
                .orElse(AuthenticationResult.forResult(AuthenticationResult.ResultType.BAD_EMAIL));
    }

    public AuthenticationResult authenticateByUsernameOrEmail(final String usernameOrEmail, final String password, final @Nullable String totpCode) {
        UserDao userDao = new UserDaoImpl(this.auth1.getDatabaseManager());
        return userDao
                .getUserByUsername(usernameOrEmail)
                .or(() -> userDao.getUserByEmail(usernameOrEmail))
                .map(user -> this.authenticate(user, password, totpCode))
                .orElse(AuthenticationResult.forResult(AuthenticationResult.ResultType.BAD_USERNAME_OR_EMAIL));
    }

    private AuthenticationResult authenticate(final User user, final String password, final @Nullable String totpCode) {
        return steps.stream()
                .map(step -> step.doStep(user, password, totpCode))
                .filter(res -> !res.passed())
                .findFirst()
                .orElse(AuthenticationStepResult.stepPassed())
                .getResult()
                .orElseGet(() -> {
                    final LoginToken token = LoginToken.withDuration(user.getId(), 23, TimeUnit.HOURS);
                    new TokenDaoImpl(this.auth1.getDatabaseManager()).saveLoginToken(token);
                    return AuthenticationResult.forSuccess(new AuthenticationToken(token.getValue(), token.getExpirationTime()));
                });
    }

    private static Iterable<Function<AuthenticationManager, AuthenticationStep>> STEPS = Arrays.asList(
            mgr -> mgr::checkRate,
            mgr -> mgr::checkPassword,
            mgr -> mgr::checkTOTP);

    private interface AuthenticationStep {
        AuthenticationStepResult doStep(User user, final String password, final @Nullable String totpCode);
    }

    private static class AuthenticationStepResult {
        private final AuthenticationResult result;

        private AuthenticationStepResult(@Nullable AuthenticationResult result) {
            this.result = result;
        }

        public static AuthenticationStepResult of(AuthenticationResult result) {
            return new AuthenticationStepResult(result);
        }

        public static AuthenticationStepResult stepPassed() {
            return new AuthenticationStepResult(null);
        }

        public boolean passed() {
            return result == null;
        }

        public Optional<AuthenticationResult> getResult() {
            return Optional.ofNullable(result);
        }
    }

    public AuthenticationStepResult checkRate(User user, final String password, final @Nullable String totpCode) {
        return AuthenticationStepResult.stepPassed(); // no rate limiting right now.
    }

    public AuthenticationStepResult checkPassword(User user, final String password, final @Nullable String totpCode) {
        final boolean match = this.auth1.getAuth1Configuration().getCheckFunction().check(password, user.getPassword());
        return match ? AuthenticationStepResult.stepPassed() :
                AuthenticationStepResult.of(AuthenticationResult.forResult(AuthenticationResult.ResultType.BAD_PASSWORD));
    }

    public AuthenticationStepResult checkTOTP(User user, final String password, final @Nullable String totpCode) {
        return AuthenticationStepResult.stepPassed(); // no TOTP right now.
    }

}
