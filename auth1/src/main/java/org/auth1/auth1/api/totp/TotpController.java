package org.auth1.auth1.api.totp;

import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.ConfirmTentativeTOTPResult;
import org.auth1.auth1.core.authentication.CreateTentativeTOTPResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TotpController {

  @Autowired
  AuthenticationManager authenticationManager;

  @RequestMapping(value = "/requestTotpSecret", method = RequestMethod.POST)
  public ResponseEntity<RequestTotpSecretResponse> requestTotp(
      @RequestParam(value = "token", required = true) String token
  ) {
    CreateTentativeTOTPResult result = authenticationManager.createTentativeTOTPSecret(token);
    return ResponseEntity.ok(RequestTotpSecretResponse.fromResult(result));
  }

  @RequestMapping(value = "/validateTotpSecret", method = RequestMethod.POST)
  public ResponseEntity<ValidateTotpSecretResponse> validateTotpSecret(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "code", required = true) String code
  ) {
    ConfirmTentativeTOTPResult result = authenticationManager
        .confirmTentativeTOTPSecret(token, code);
    return ResponseEntity.ok(ValidateTotpSecretResponse.fromResult(result));
  }
}
