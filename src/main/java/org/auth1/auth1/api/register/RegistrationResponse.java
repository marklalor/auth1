package org.auth1.auth1.api.register;

import org.auth1.auth1.core.authentication.RegistrationResult;

public class RegistrationResponse {
    private final RegistrationResult result;

    public RegistrationResponse(RegistrationResult result) {
        this.result = result;
    }

    public RegistrationResult getResult() {
        return result;
    }
}
