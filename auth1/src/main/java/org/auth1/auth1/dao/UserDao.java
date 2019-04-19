package org.auth1.auth1.dao;

import org.auth1.auth1.core.authentication.UserIdentifier;
import org.auth1.auth1.err.EmailAlreadyExistsException;
import org.auth1.auth1.err.UsernameAlreadyExistsException;
import org.auth1.auth1.err.UserDoesNotExistException;
import org.auth1.auth1.model.entities.User;

import java.util.Optional;

public interface UserDao {
    void saveUser(final User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException;

    void lockUser(final UserIdentifier userIdentifier);

    void unlockUser(final UserIdentifier userIdentifier);

    void resetPassword(final UserIdentifier userIdentifier, final String password) throws UserDoesNotExistException;

    Optional<User> getUserById(final int userId);

    Optional<User> getUserByUsername(final String username);

    Optional<User> getUserByEmail(final String email);

    Optional<User> getUserByUsernameOrEmail(final String usernameOrEmail);


}
