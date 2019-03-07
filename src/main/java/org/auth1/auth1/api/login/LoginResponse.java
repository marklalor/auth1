package org.auth1.auth1.api.login;

import org.auth1.auth1.core.authentication.AuthenticationResult;
import org.auth1.auth1.core.authentication.ExpiringToken;

import javax.annotation.Nullable;

public class LoginResponse {
    private final AuthenticationResult.ResultType resultType;
    private final @Nullable ExpiringToken token;

    LoginResponse(AuthenticationResult.ResultType resultType, @Nullable ExpiringToken userAuthenticationToken) {
        this.resultType = resultType;
        this.token = userAuthenticationToken;
    }

    public static LoginResponse fromAuthenticationResult(AuthenticationResult result) {
        return new LoginResponse(result.getType(), result.getUserAuthenticationToken());
    }

    public AuthenticationResult.ResultType getResultType() {
        return resultType;
    }

    @Nullable
    public ExpiringToken getToken() {
        return token;
    }
}
