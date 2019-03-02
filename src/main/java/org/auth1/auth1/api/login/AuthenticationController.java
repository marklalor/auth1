package org.auth1.auth1.api.login;

import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.AuthenticationResult;
import org.auth1.auth1.core.authentication.CheckAuthenticationTokenResult;
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
        AuthenticationResult result;

        // Exactly one of {email, username, usernameOrEmail} should be set
        if (usernameOrEmail != null && (username == null && email == null)) {
            result = authenticationManager.authenticateByUsernameOrEmail(usernameOrEmail, password, totpCode);
        } else if (username != null && (email == null && usernameOrEmail == null)) {
            result = authenticationManager.authenticateByUsername(username, password, totpCode);
        } else if (email != null && (username == null && usernameOrEmail == null)) {
            result = authenticationManager.authenticateByEmail(email, password, totpCode);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(LoginResponse.fromAuthenticationResult(result));
    }

    @RequestMapping(value = "/checkAuthToken", method = RequestMethod.POST)
    public ResponseEntity<CheckAuthenticationTokenResponse> checkAuthToken(@RequestParam(value = "token", required =
            true) String token) {
        CheckAuthenticationTokenResult result = authenticationManager.checkAuthenticationToken(token);

        return ResponseEntity.ok(CheckAuthenticationTokenResponse.fromResult(result));
    }
}
