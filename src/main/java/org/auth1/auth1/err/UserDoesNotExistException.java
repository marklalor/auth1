package org.auth1.auth1.err;

public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException(String missingUser) {
        super(String.format("User %s does not exist", missingUser));
    }
}
