package org.auth1.auth1.api.totp;

import java.time.ZonedDateTime;
import javax.annotation.Nullable;
import org.auth1.auth1.core.authentication.CreateTentativeTOTPResult;
import org.auth1.auth1.core.authentication.CreateTentativeTOTPResult.ResultType;

public class RequestTotpSecretResponse {

  private final ResultType resultType;
  private final @Nullable
  byte[] tentativeTotpSecret;
  private final @Nullable
  ZonedDateTime expirationTime;

  public RequestTotpSecretResponse(
      ResultType resultType, @Nullable byte[] tentativeTotpSecret,
      @Nullable ZonedDateTime expirationTime) {
    this.resultType = resultType;
    this.tentativeTotpSecret = tentativeTotpSecret;
    this.expirationTime = expirationTime;
  }

  public static RequestTotpSecretResponse fromResult(CreateTentativeTOTPResult result) {
    return new RequestTotpSecretResponse(result.getType(), result.getTentativeTotpSecret(),
        result.getExpirationTime());
  }

  public ResultType getResultType() {
    return resultType;
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
