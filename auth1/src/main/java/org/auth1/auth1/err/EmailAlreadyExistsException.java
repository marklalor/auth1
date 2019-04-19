package org.auth1.auth1.err;

public class EmailAlreadyExistsException extends Exception {
    public EmailAlreadyExistsException(String email) {
        super(String.format("Email %s already exists", email));
    }
}
