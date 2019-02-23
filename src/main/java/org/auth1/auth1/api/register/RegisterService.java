package org.auth1.auth1.api.register;

import org.auth1.auth1.api.register.RegistrationResponse;
import org.auth1.auth1.core.authentication.RegistrationResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterService {
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RegistrationResponse register(@RequestParam(value="username", required=true) String username,
                                         @RequestParam(value="email", required=true) String email,
                                         @RequestParam(value="password", required=true) String password) {
        return new RegistrationResponse(RegistrationResult.SERVICE_UNAVAILABLE);
    }
}
