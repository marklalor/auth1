package org.auth1.auth1.dao;

import org.auth1.auth1.core.authentication.UserIdentifier;
import org.auth1.auth1.err.UserDoesNotExistException;
import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.User;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.Optional;

public abstract class UnimplementedUserDao implements UserDao {
    @Override
    public void saveUser(User user) {
        throw new NotYetImplementedException();
    }

    @Override
    public void lockUser(UserIdentifier userIdentifier) {
        throw new NotYetImplementedException();
    }

    @Override
    public void unlockUser(UserIdentifier userIdentifier) {
        throw new NotYetImplementedException();
    }

    @Override
    public void resetPassword(UserIdentifier userIdentifier, String password) throws UserDoesNotExistException {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<User> getUserById(int userId) {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail) {
        throw new NotYetImplementedException();
    }
}
