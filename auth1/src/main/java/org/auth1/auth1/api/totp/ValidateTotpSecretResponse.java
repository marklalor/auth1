package org.auth1.auth1.api.totp;

import org.auth1.auth1.core.authentication.ConfirmTentativeTOTPResult;

public class ValidateTotpSecretResponse {

  private final ConfirmTentativeTOTPResult resultType;

  public ValidateTotpSecretResponse(
      ConfirmTentativeTOTPResult resultType) {
    this.resultType = resultType;
  }

  public static ValidateTotpSecretResponse fromResult(ConfirmTentativeTOTPResult result) {
    return new ValidateTotpSecretResponse(result);
  }

  public ConfirmTentativeTOTPResult getResultType() {
    return resultType;
  }
}
