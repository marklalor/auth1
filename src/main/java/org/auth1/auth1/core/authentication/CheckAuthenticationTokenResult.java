package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;

public class CheckAuthenticationTokenResult {
    private final boolean valid;
    private final @Nullable Integer userId;

    public CheckAuthenticationTokenResult(boolean valid, @Nullable Integer userId) {
        this.valid = valid;
        this.userId = userId;
    }

    public boolean isValid() {
        return valid;
    }

    public Integer getUserId() {
        if (!isValid()) {
            throw new RuntimeException("Cannot get user ID from an invalid authentication token result.");
        }
        return userId;
    }

    public static CheckAuthenticationTokenResult forSuccess(int userId) {
        return new CheckAuthenticationTokenResult(true, userId);
    }

    public static CheckAuthenticationTokenResult forInvalid() {
        return new CheckAuthenticationTokenResult(false, null);
    }
}
