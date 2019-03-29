package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;

/**
 * Result of calling {@link AuthenticationManager#authenticate(UserIdentifier, String, String)}.
 * See the constants of {@link ResultType} for details on the various states this class can take on.
 */
public class CreateTentativeTOTPResult {
    public static final CreateTentativeTOTPResult INVALID_TOKEN
            = new CreateTentativeTOTPResult(ResultType.INVALID_TOKEN, null);

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


    private CreateTentativeTOTPResult(ResultType type, @Nullable byte[] tentativeTotpSecret) {
        this.type = type;
        this.tentativeTotpSecret = tentativeTotpSecret;
    }

    public static CreateTentativeTOTPResult forSuccess(byte[] tentativeTotpSecret) {
        return new CreateTentativeTOTPResult(ResultType.SUCCESS, tentativeTotpSecret);
    }

    public ResultType getType() {
        return type;
    }

    @Nullable
    public byte[] getTentativeTotpSecret() {
        return tentativeTotpSecret;
    }
}
