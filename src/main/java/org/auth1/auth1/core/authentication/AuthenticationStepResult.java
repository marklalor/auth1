package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Required return value for authentication steps. Steps should return {@link #stepPassed()} if they succeeded,
 * and a result specified by {@link #of(AuthenticationResult)} if they should terminate the sequence and return the
 * provided {@link AuthenticationResult}.
 */
class AuthenticationStepResult {
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

    public boolean failed() {
        return result != null;
    }

    public Optional<AuthenticationResult> getResult() {
        return Optional.ofNullable(result);
    }
}
