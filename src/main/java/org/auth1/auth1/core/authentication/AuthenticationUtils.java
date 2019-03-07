package org.auth1.auth1.core.authentication;

import java.security.SecureRandom;
import java.util.function.Function;

public class AuthenticationUtils {

    public static String generateToken(int numBytes, Function<byte[], String> encodingFunction) {
        final byte[] bytes = new byte[numBytes];
        new SecureRandom().nextBytes(bytes);
        return encodingFunction.apply(bytes);
    }

}
