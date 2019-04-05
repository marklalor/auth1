package org.auth1.auth1.api.passwordReset;

import javax.annotation.Nullable;
import org.auth1.auth1.core.authentication.ExpiringToken;
import org.auth1.auth1.core.authentication.GeneratePasswordResetTokenResult;
import org.auth1.auth1.core.authentication.GeneratePasswordResetTokenResult.ResultType;

public class GetPasswordResetTokenResponse {

  private final ResultType resultType;
  private final @Nullable
  ExpiringToken token;

  public GetPasswordResetTokenResponse(ResultType resultType, @Nullable ExpiringToken token) {
    this.resultType = resultType;
    this.token = token;
  }

  public static GetPasswordResetTokenResponse fromResult(GeneratePasswordResetTokenResult result) {
    return new GetPasswordResetTokenResponse(result.getType(), result.getPasswordResetToken());
  }

  public ResultType getResultType() {
    return resultType;
  }

  public ExpiringToken getToken() {
    return token;
  }
}
