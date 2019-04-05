package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;

/**
 * <p>Result of calling {@link AuthenticationManager#checkAuthenticationToken(String)} that describes a
 * supplied token string.</p>
 *
 * <p>If the token was valid (associated with a user, and not expired), then {@link #isValid()} will
 * return <code>true</code> and {@link #getUserId()} will return the id of the user associated with the token.</p>
 *
 * <p>If the token was not valid (not associated with a user, or expired), then {@link #isValid()} will return
 * <code>false</code> and {@link #getUserId()} will not be callable.</p>
 */
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
