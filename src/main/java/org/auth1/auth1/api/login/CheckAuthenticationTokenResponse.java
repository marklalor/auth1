package org.auth1.auth1.api.login;

import org.auth1.auth1.core.authentication.CheckAuthenticationTokenResult;

import javax.annotation.Nullable;

public class CheckAuthenticationTokenResponse {
    private final boolean isValid;
    private final @Nullable
    Integer username;

    CheckAuthenticationTokenResponse(boolean isValid, @Nullable Integer username) {
        this.isValid = isValid;
        this.username = username;
    }

    static CheckAuthenticationTokenResponse fromResult(CheckAuthenticationTokenResult result) {
        return new CheckAuthenticationTokenResponse(result.isValid(), result.getUserId());
    }

    public boolean isValid() {
        return isValid;
    }

    @Nullable
    public Integer getUsername() {
        return username;
    }
}
