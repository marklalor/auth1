package org.auth1.auth1.dao;

import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.LoginToken;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.model.entities.UserToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    public void saveLoginToken(LoginToken token) {
        throw new NotYetImplementedException();
    }
}
