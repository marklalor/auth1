package org.auth1.auth1.core.authentication;

import java.time.ZonedDateTime;

public class AuthenticationToken {
    private final String tokenValue;
    private final ZonedDateTime expirationTime;

    public AuthenticationToken(String tokenValue, ZonedDateTime expirationTime) {
        this.tokenValue = tokenValue;
        this.expirationTime = expirationTime;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public ZonedDateTime getExpirationTime() {
        return expirationTime;
    }
}
