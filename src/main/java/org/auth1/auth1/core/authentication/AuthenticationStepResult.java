package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;
import java.util.Optional;

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

    public Optional<AuthenticationResult> getResult() {
        return Optional.ofNullable(result);
    }
}
