package org.auth1.auth1.core.authentication;

import org.auth1.auth1.dao.UserDao;
import org.auth1.auth1.dao.UserDaoImpl;
import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.User;
import org.hibernate.cfg.NotYetImplementedException;

import javax.annotation.Nullable;

public class AuthenticationManager {

    private final DatabaseManager databaseManager;

    public AuthenticationManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public RegistrationResult register(final String username, final String email, final String password) {
        throw new NotYetImplementedException();
    }

    public AuthenticationResult authenticateByUsername(final String username, final String password, final @Nullable String totpCode) {
        return new UserDaoImpl(this.databaseManager)
                .getUserByUsername(username)
                .map(user -> this.authenticate(user, password, totpCode))
                .orElse(AuthenticationResult.forResult(AuthenticationResult.ResultType.BAD_USERNAME));
    }

    public AuthenticationResult authenticateByEmail(final String email, final String password, final @Nullable String totpCode) {
        return new UserDaoImpl(this.databaseManager)
                .getUserByEmail(email)
                .map(user -> this.authenticate(user, password, totpCode))
                .orElse(AuthenticationResult.forResult(AuthenticationResult.ResultType.BAD_EMAIL));
    }

    public AuthenticationResult authenticateByUsernameOrEmail(final String usernameOrEmail, final String password, final @Nullable String totpCode) {
        UserDao userDao = new UserDaoImpl(this.databaseManager);
        return userDao
                .getUserByUsername(usernameOrEmail)
                .or(() -> userDao.getUserByEmail(usernameOrEmail))
                .map(user -> this.authenticate(user, password, totpCode))
                .orElse(AuthenticationResult.forResult(AuthenticationResult.ResultType.BAD_USERNAME_OR_EMAIL));
    }

    private AuthenticationResult authenticate(final User user, final String password, final @Nullable String totpCode) {

        throw new NotYetImplementedException();
    }

}
