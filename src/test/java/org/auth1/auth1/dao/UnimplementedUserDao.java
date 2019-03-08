package org.auth1.auth1.dao;

import org.auth1.auth1.model.entities.User;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.Optional;

public abstract class UnimplementedUserDao implements UserDao {
    @Override
    public boolean login(String username, String password) {
        throw new NotYetImplementedException();
    }

    @Override
    public void saveUser(User user) {
        throw new NotYetImplementedException();
    }

    @Override
    public void setPasswordResetToken(String username, String passwordResetToken) {
        throw new NotYetImplementedException();
    }

    @Override
    public void lockUser(String username) {
        throw new NotYetImplementedException();
    }

    @Override
    public void unlockUser(String username) {
        throw new NotYetImplementedException();
    }

    @Override
    public void resetPassword(String username, String password) {
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
