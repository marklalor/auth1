package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;

public class AuthenticationResult {
    public enum ResultType {
        SUCCESS,
        USERNAME_DOES_NOT_EXIST,
        EMAIL_DOES_NOT_EXIST,
        USERNAME_OR_EMAIL_DOES_NOT_EXIST,
        BAD_PASSWORD,
        TOO_MANY_REQUESTS,
        NOT_VERIFIED,
        ACCOUNT_LOCKED
    }

    private final ResultType type;
    private final @Nullable AuthenticationToken authenticationToken;
    private final @Nullable Double rateLimit; // number of logins per period/window (milliseconds)
    private final @Nullable Double ratePeriod; // milliseconds of the login period/window

    private AuthenticationResult(ResultType type, @Nullable AuthenticationToken authenticationToken, @Nullable Double rateLimit, @Nullable Double ratePeriod) {
        this.type = type;
        this.authenticationToken = authenticationToken;
        this.rateLimit = rateLimit;
        this.ratePeriod = ratePeriod;
    }

    public static AuthenticationResult forSuccess(AuthenticationToken token) {
        return new AuthenticationResult(ResultType.SUCCESS, token, null, null);
    }

    public static AuthenticationResult forTooManyRequests(double rateLimit, double ratePeriod) {
        return new AuthenticationResult(ResultType.TOO_MANY_REQUESTS, null, rateLimit, ratePeriod);
    }

    public static AuthenticationResult forResult(ResultType type) {
        if (type == ResultType.SUCCESS || type == ResultType.TOO_MANY_REQUESTS) {
            throw new RuntimeException("ResultType.SUCCESS and ResultType.TOO_MANY_REQUESTS should be handled " +
                    "with .forSuccess and .forTooManyRequests");
        }

        return new AuthenticationResult(type, null, null, null);
    }
}
