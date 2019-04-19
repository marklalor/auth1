package org.auth1.auth1.err;

public class UsernameAlreadyExistsException extends Exception {
    public UsernameAlreadyExistsException(String duplicateUsername) {
        super(String.format("User %s already exists", duplicateUsername));
    }
}
