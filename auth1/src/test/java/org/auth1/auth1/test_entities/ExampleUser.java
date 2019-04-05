package org.auth1.auth1.test_entities;

import org.auth1.auth1.model.entities.User;

import java.time.ZonedDateTime;

public class ExampleUser {
    public static final String USERNAME = "user";
    public static final String PASSWORD = "pass";
    public static final String altPassword = "altPass";
    public static final byte[] TOTP_SECRET = new byte[128];
    public static final String EMAIL = "email@EMAIL.co";
    public static final boolean VERIFIED = false;
    public static final boolean LOCKED = false;
    public static final ZonedDateTime CREATION_TIME = ZonedDateTime.now();
    public static final User INSTANCE = new User(USERNAME, PASSWORD, TOTP_SECRET, EMAIL, VERIFIED, LOCKED, CREATION_TIME);
}
