package org.auth1.auth1.test_entities;

import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.UserAuthenticationToken;

import java.time.ZonedDateTime;

public class ExamplePasswordResetToken {
    public static final String VALUE = "tokenValue";
    public static final int USER_ID = 1; // Must correspond to the first user id inserted in User table
    public static final ZonedDateTime ISSUE_TIME = ZonedDateTime.now();
    public static final ZonedDateTime EXP_TIME = ZonedDateTime.now().plusSeconds(600);

    public static final PasswordResetToken INSTANCE = new PasswordResetToken(VALUE, USER_ID, ISSUE_TIME, EXP_TIME);
}
