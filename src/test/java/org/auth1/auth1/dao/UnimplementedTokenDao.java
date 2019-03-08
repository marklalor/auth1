package org.auth1.auth1.dao;

import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.UserAuthenticationToken;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.Optional;

public abstract class UnimplementedTokenDao implements TokenDao {
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
        throw new NotYetImplementedException();
    }
}
