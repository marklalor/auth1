package org.auth1.auth1.api.login;

import java.util.Objects;
import java.util.stream.Stream;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.AuthenticationResult;
import org.auth1.auth1.core.authentication.CheckAuthenticationTokenResult;
import org.auth1.auth1.core.authentication.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@RequestParam(value = "usernameOrEmail", required = false) String usernameOrEmail,
                                               @RequestParam(value = "username", required = false) String username,
                                               @RequestParam(value = "email", required = false) String email,
                                               @RequestParam(value = "password", required = true) String password,
                                               @RequestParam(value = "totpCode", required = false) String totpCode) {
        if (Stream.of(username, email, usernameOrEmail).filter(Objects::nonNull).count() != 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            final UserIdentifier userId = UserIdentifier.forOneOf(username, email, usernameOrEmail);
            final AuthenticationResult result = authenticationManager.authenticate(userId, password, totpCode, null, null);
            return ResponseEntity.ok(LoginResponse.fromAuthenticationResult(result));
        }
    }

    @RequestMapping(value = "/checkAuthToken", method = RequestMethod.POST)
    public ResponseEntity<CheckAuthenticationTokenResponse> checkAuthToken(@RequestParam(value = "token", required =
            true) String token) {
        CheckAuthenticationTokenResult result = authenticationManager.checkAuthenticationToken(token);

        return ResponseEntity.ok(CheckAuthenticationTokenResponse.fromResult(result));
    }
}
