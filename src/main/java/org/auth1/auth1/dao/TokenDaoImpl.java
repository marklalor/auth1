package org.auth1.auth1.dao;

import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.model.entities.UserAuthenticationToken;
import org.auth1.auth1.util.DBUtils;
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
    public void saveLoginToken(UserAuthenticationToken token) {
        DBUtils.saveEntity(databaseManager, token);
    }

    @Override
    public void savePasswordResetToken(PasswordResetToken token) {
        DBUtils.saveEntity(databaseManager, token);
    }

    @Override
    public void logout(UserAuthenticationToken token) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.delete(token);
    }

    @Override
    public Optional<UserAuthenticationToken> getAuthToken(String token) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        final Object obj = session.createQuery("FROM UserAuthenticationToken u WHERE u.value = :value")
                .setParameter("value", token)
                .uniqueResult();
        if(obj instanceof UserAuthenticationToken)
            return Optional.of((UserAuthenticationToken) obj);
        else
            return Optional.empty();
    }

    @Override
    public Optional<PasswordResetToken> getPasswordResetToken(String token) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        final Object obj = session.createQuery("FROM PasswordResetToken u WHERE u.value = :value")
                .setParameter("value", token)
                .uniqueResult();
        if(obj instanceof PasswordResetToken)
            return Optional.of((PasswordResetToken) obj);
        else
            return Optional.empty();
    }


}
