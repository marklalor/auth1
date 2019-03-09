package org.auth1.auth1.core;

import org.auth1.auth1.core.authentication.AuthenticationManager;
import org.auth1.auth1.core.authentication.GeneratePasswordResetTokenResult;
import org.auth1.auth1.core.authentication.UserIdentifier;
import org.auth1.auth1.dao.UnimplementedTokenDao;
import org.auth1.auth1.dao.UnimplementedUserDao;
import org.auth1.auth1.model.Auth1Configuration;
import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class ResetPasswordTest {
    public static Auth1Configuration BASIC_CONFIG = new Auth1Configuration(
            Auth1Configuration.HashAlgorithm.BCRYPT,
            14,-1, -1, -1,
            Auth1Configuration.RequiredUserFields.USERNAME_AND_EMAIL, false);

    private static final String USERNAME = "username";
    private static final String BAD_USERNAME = "nonexistant_username";
    private static final String EMAIL = "email@gmail.com";
    private static final String PASSWORD = "password";

    public static int expectedBase64EncodeLength(int bytes) {
        var numCharacters = (bytes * 8) / 6; // 6 bits per character
        var roundedUp = ((numCharacters + 3 ) / 4 ) * 4; // base 64 pads, round up nearest mult of 4
        return roundedUp;
    }

    @Test
    public void testPasswordResetTokenLength() {
        var someToken = PasswordResetToken.withDuration(0, 1, TimeUnit.DAYS);
        var expected = expectedBase64EncodeLength(16); // 16 bytes, base 64 encoded (log(64)=6 bits per character
        Assert.assertEquals(expected, someToken.getValue().length());
    }

    @Test
    public void testGeneratePasswordResetToken() {
        final boolean[] reachedSaveBlock = {false};
        final AuthenticationManager manager = new AuthenticationManager(BASIC_CONFIG, new UnimplementedUserDao() {
            @Override
            public Optional<User> getUserByUsername(String username) {
                if (!username.equals(BAD_USERNAME)) {
                    return Optional.of(new User(username, BASIC_CONFIG.getHashFunction().hash(PASSWORD), null, EMAIL, false, false, ZonedDateTime.now()));
                } else {
                    return Optional.empty();
                }
            }
        }, new UnimplementedTokenDao() {
            @Override
            public void savePasswordResetToken(PasswordResetToken token) {
                reachedSaveBlock[0] = true;
            }
        });
        var result = manager.generatePasswordResetToken(UserIdentifier.forUsername(USERNAME));
        Assert.assertEquals(result.getType(), GeneratePasswordResetTokenResult.ResultType.SUCCESS);
        Assert.assertTrue(result.getPasswordResetToken().getExpirationTime().isAfter(ZonedDateTime.now()));
        Assert.assertTrue(reachedSaveBlock[0]);
    }
}