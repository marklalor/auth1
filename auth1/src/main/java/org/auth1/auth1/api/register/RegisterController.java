package org.auth1.auth1.api.register;

import org.auth1.auth1.api.register.RegistrationResponse;
import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.RegistrationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<RegistrationResponse> register(@RequestParam(value="username", required=true) String username,
                                                         @RequestParam(value="email", required=true) String email,
                                                         @RequestParam(value="password", required=true) String password) {
        RegistrationResult result = authenticationManager.register(username, email, password);
        RegistrationResponse response = new RegistrationResponse(result);

        if (result == RegistrationResult.SERVICE_UNAVAILABLE) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}
