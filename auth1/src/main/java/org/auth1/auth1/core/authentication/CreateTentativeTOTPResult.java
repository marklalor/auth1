package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;

/**
 * Result of calling {@link AuthenticationManager#authenticate(UserIdentifier, String, String)}.
 * See the constants of {@link ResultType} for details on the various states this class can take on.
 */
public class CreateTentativeTOTPResult {
    public static final CreateTentativeTOTPResult INVALID_TOKEN
            = new CreateTentativeTOTPResult(ResultType.INVALID_TOKEN, null, null);


    public enum ResultType {
        /**
         * A TOTP code was created and is
         */
        SUCCESS,
        /**
         * Too many authentication requests were made in a recent window of time so the request was not processed.
         */
        INVALID_TOKEN
    }

    private final ResultType type;
    private final @Nullable byte[] tentativeTotpSecret;
    private final @Nullable ZonedDateTime expirationTime;


    private CreateTentativeTOTPResult(ResultType type, @Nullable byte[] tentativeTotpSecret, ZonedDateTime expirationTime) {
        this.type = type;
        this.tentativeTotpSecret = tentativeTotpSecret;
        this.expirationTime = expirationTime;
    }

    public static CreateTentativeTOTPResult forSuccess(byte[] tentativeTotpSecret, ZonedDateTime expirationTime) {
        return new CreateTentativeTOTPResult(ResultType.SUCCESS, tentativeTotpSecret, expirationTime);
    }

    public ResultType getType() {
        return type;
    }

    @Nullable
    public byte[] getTentativeTotpSecret() {
        return tentativeTotpSecret;
    }

    @Nullable
    public ZonedDateTime getExpirationTime() {
        return expirationTime;
    }
}
