package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;

public class AuthenticationResult {
    public static final AuthenticationResult USER_DOES_NOT_EXIST
            = new AuthenticationResult(ResultType.USER_DOES_NOT_EXIST, null, null, null);
    public static final AuthenticationResult BAD_PASSWORD
            = new AuthenticationResult(ResultType.BAD_PASSWORD, null, null, null);
    public static final AuthenticationResult NOT_VERIFIED
            = new AuthenticationResult(ResultType.NOT_VERIFIED, null, null, null);
    public static final AuthenticationResult ACCOUNT_LOCKED
            = new AuthenticationResult(ResultType.ACCOUNT_LOCKED, null, null, null);


    public enum ResultType {
        SUCCESS,
        TOO_MANY_REQUESTS,
        USER_DOES_NOT_EXIST,
        BAD_PASSWORD,
        NOT_VERIFIED,
        ACCOUNT_LOCKED
    }

    private final ResultType type;
    private final @Nullable ExpiringToken userAuthenticationToken;
    private final @Nullable Double rateLimit; // number of logins per period/window (milliseconds)
    private final @Nullable Double ratePeriod; // milliseconds of the login period/window

    private AuthenticationResult(ResultType type, @Nullable ExpiringToken userAuthenticationToken, @Nullable Double rateLimit, @Nullable Double ratePeriod) {
        this.type = type;
        this.userAuthenticationToken = userAuthenticationToken;
        this.rateLimit = rateLimit;
        this.ratePeriod = ratePeriod;
    }

    public static AuthenticationResult forSuccess(ExpiringToken userAuthenticationToken) {
        return new AuthenticationResult(ResultType.SUCCESS, userAuthenticationToken, null, null);
    }

    public static AuthenticationResult forTooManyRequests(double rateLimit, double ratePeriod) {
        return new AuthenticationResult(ResultType.TOO_MANY_REQUESTS, null, rateLimit, ratePeriod);
    }


    public ResultType getType() {
        return type;
    }

    @Nullable
    public ExpiringToken getUserAuthenticationToken() {
        return userAuthenticationToken;
    }

    @Nullable
    public Double getRateLimit() {
        return rateLimit;
    }

    @Nullable
    public Double getRatePeriod() {
        return ratePeriod;
    }
}
