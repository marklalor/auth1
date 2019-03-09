package org.auth1.auth1.api.passwordReset;

import org.auth1.auth1.core.authentication.ResetPasswordResult;

public class ResetPasswordResponse {

  private final ResetPasswordResult result;

  public ResetPasswordResponse(ResetPasswordResult result) {
    this.result = result;
  }

  public static ResetPasswordResponse fromResult(ResetPasswordResult result) {
    return new ResetPasswordResponse(result);
  }

  public ResetPasswordResult getResult() {
    return result;
  }
}
