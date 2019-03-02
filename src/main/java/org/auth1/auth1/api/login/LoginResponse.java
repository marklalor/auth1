package org.auth1.auth1.api.login;

import org.auth1.auth1.core.authentication.AuthenticationResult;
import org.auth1.auth1.core.authentication.AuthenticationToken;

import javax.annotation.Nullable;

public class LoginResponse {
    private final AuthenticationResult.ResultType resultType;
    private final @Nullable
    AuthenticationToken token;

    LoginResponse(AuthenticationResult.ResultType resultType, @Nullable AuthenticationToken token) {
        this.resultType = resultType;
        this.token = token;
    }

    static LoginResponse fromAuthenticationResult(AuthenticationResult result) {
        return new LoginResponse(result.getType(), result.getAuthenticationToken());
    }

    public AuthenticationResult.ResultType getResultType() {
        return resultType;
    }

    @Nullable
    public AuthenticationToken getToken() {
        return token;
    }
}
