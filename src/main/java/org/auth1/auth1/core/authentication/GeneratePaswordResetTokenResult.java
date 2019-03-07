package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;

public class GeneratePaswordResetTokenResult extends Result<GeneratePaswordResetTokenResult.ResultType> {

    public static final GeneratePaswordResetTokenResult ACCOUNT_DOES_NOT_EXIST
            = new GeneratePaswordResetTokenResult(ResultType.ACCOUNT_DOES_NOT_EXIST, null);
    public static final GeneratePaswordResetTokenResult ACCOUNT_LOCKED
            = new GeneratePaswordResetTokenResult(ResultType.ACCOUNT_LOCKED, null);

    public static GeneratePaswordResetTokenResult forSuccess(ExpiringToken token) {
        return new GeneratePaswordResetTokenResult(ResultType.SUCCESS, token);
    }

    public static GeneratePaswordResetTokenResult forSuccessAlreadySent(ExpiringToken token) {
        return new GeneratePaswordResetTokenResult(ResultType.SUCCESS_ALREADY_SENT, token);
    }

    public enum ResultType {
        SUCCESS,
        SUCCESS_ALREADY_SENT,
        ACCOUNT_DOES_NOT_EXIST,
        ACCOUNT_LOCKED
    }

    private final @Nullable ExpiringToken passwordResetToken;

    private GeneratePaswordResetTokenResult(ResultType type, @Nullable ExpiringToken passwordResetToken) {
        super(type);
        this.passwordResetToken = passwordResetToken;
    }

    public ExpiringToken getPasswordResetToken() {
        requireAny(ResultType.SUCCESS, ResultType.SUCCESS_ALREADY_SENT);
        return passwordResetToken;
    }
}
