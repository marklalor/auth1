package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;

public class GeneratePasswordResetTokenResult extends
    Result<GeneratePasswordResetTokenResult.ResultType> {

  public static final GeneratePasswordResetTokenResult ACCOUNT_DOES_NOT_EXIST
      = new GeneratePasswordResetTokenResult(ResultType.ACCOUNT_DOES_NOT_EXIST, null);
  public static final GeneratePasswordResetTokenResult ACCOUNT_LOCKED
      = new GeneratePasswordResetTokenResult(ResultType.ACCOUNT_LOCKED, null);
  private final @Nullable
  ExpiringToken passwordResetToken;

  private GeneratePasswordResetTokenResult(ResultType type,
      @Nullable ExpiringToken passwordResetToken) {
    super(type);
    this.passwordResetToken = passwordResetToken;
  }

  public static GeneratePasswordResetTokenResult forSuccess(ExpiringToken token) {
    return new GeneratePasswordResetTokenResult(ResultType.SUCCESS, token);
  }

  public static GeneratePasswordResetTokenResult forSuccessAlreadySent(ExpiringToken token) {
    return new GeneratePasswordResetTokenResult(ResultType.SUCCESS_ALREADY_SENT, token);
  }

  public ExpiringToken getPasswordResetToken() {
    requireAny(ResultType.SUCCESS, ResultType.SUCCESS_ALREADY_SENT);
    return passwordResetToken;
  }

  public enum ResultType {
    SUCCESS,
    SUCCESS_ALREADY_SENT,
    ACCOUNT_DOES_NOT_EXIST,
    ACCOUNT_LOCKED
  }
}
