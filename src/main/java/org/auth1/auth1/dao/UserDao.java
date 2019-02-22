package org.auth1.auth1.dao;

import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.model.entities.UserToken;

public interface UserDao {
    UserToken login(final String username, final String password);

    void register(final User user);

    void setPasswordResetToken(final String username, final String passwordResetToken);

    void lockUser(final String username);

    void unlockUser(final String username);

    void resetPassword(final String username, final String password);

}
