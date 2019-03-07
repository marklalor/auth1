package org.auth1.auth1.dao;

import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.UserAuthenticationToken;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TokenDaoImpl implements TokenDao {

    private final DatabaseManager databaseManager;

    public TokenDaoImpl(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void saveLoginToken(UserAuthenticationToken token) {
        throw new NotYetImplementedException();
    }

    @Override
    public void savePasswordResetToken(PasswordResetToken token) {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<UserAuthenticationToken> getToken(String token) {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<PasswordResetToken> getPasswordResetToken(String token) {
        return Optional.empty();
    }

}
