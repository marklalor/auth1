package org.auth1.auth1.core;

import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.RegistrationResult;
import org.auth1.auth1.dao.UnimplementedUserDao;
import org.auth1.auth1.model.Auth1Configuration;
import org.auth1.auth1.model.entities.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class RegisterTest {
    public static Auth1Configuration BASIC_CONFIG = new Auth1Configuration(
            Auth1Configuration.HashAlgorithm.BCRYPT,
            14,-1, -1, -1,
            Auth1Configuration.RequiredUserFields.USERNAME_AND_EMAIL, false);

    private static final String USERNAME = "username";
    private static final String EMAIL = "email@gmail.com";
    private static final String PASSWORD = "password";

    public void testRegister(Auth1Configuration.HashAlgorithm algorithm) {
        var config = new Auth1Configuration(
                algorithm, 14, 14,8, 1,
                Auth1Configuration.RequiredUserFields.USERNAME_AND_EMAIL, false);
        final AuthenticationManager manager = new AuthenticationManager(config, new UnimplementedUserDao() {
            @Override
            public void saveUser(User user) {
                Assert.assertEquals(USERNAME, user.getUsername());
                Assert.assertEquals(EMAIL, user.getEmail());
                Assert.assertEquals(false, user.isVerified());
                Assert.assertEquals(null, user.getTotpSecret());
                Assert.assertTrue("Hashed passwords do not match",
                        config.getCheckFunction().check(PASSWORD, user.getPassword()));
            }
        }, null);
        var result = manager.register(USERNAME, EMAIL, PASSWORD);
        Assert.assertEquals(result, RegistrationResult.SUCCESS);
    }

    @Test
    public void testRegisterBcrypt() {
        testRegister(Auth1Configuration.HashAlgorithm.BCRYPT);
    }

    @Test
    public void testRegisterScrypt() {
        testRegister(Auth1Configuration.HashAlgorithm.SCRYPT);
    }
}