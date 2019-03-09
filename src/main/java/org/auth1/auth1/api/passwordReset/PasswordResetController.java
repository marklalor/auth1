package org.auth1.auth1.api.passwordReset;

import java.util.Objects;
import java.util.stream.Stream;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.GeneratePasswordResetTokenResult;
import org.auth1.auth1.core.authentication.ResetPasswordResult;
import org.auth1.auth1.core.authentication.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordResetController {

  @Autowired
  AuthenticationManager authenticationManager;

  @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
  public ResponseEntity<ResetPasswordResponse> resetPassword(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "newPassword", required = true) String password
  ) {
    ResetPasswordResult result = authenticationManager.resetPassword(token, password);
    ResetPasswordResponse response = ResetPasswordResponse.fromResult(result);

    return ResponseEntity.ok(response);
  }

  @RequestMapping(value = "/getPasswordResetToken", method = RequestMethod.POST)
  public ResponseEntity<GetPasswordResetTokenResponse> getPasswordResetToken(
      @RequestParam(value = "usernameOrEmail", required = false) String usernameOrEmail,
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "email", required = false) String email
  ) {
    if (Stream.of(username, email, usernameOrEmail).filter(Objects::nonNull).count() > 1) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    UserIdentifier userId = UserIdentifier.forOneOf(username, email, usernameOrEmail);
    GeneratePasswordResetTokenResult result = authenticationManager
        .generatePasswordResetToken(userId);
    GetPasswordResetTokenResponse response = GetPasswordResetTokenResponse.fromResult(result);

    return ResponseEntity.ok(response);
  }
}
