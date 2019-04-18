package org.auth1.auth1.dao;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.model.entities.UserAuthenticationToken;
import org.auth1.auth1.util.DBUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.hibernate.exception.ConstraintViolationException;
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
        try {
            DBUtils.saveEntity(databaseManager, token);
        } catch (ConstraintViolationException e) {
            e.printStackTrace(); // TODO: Handle duplicate login token
        }
    }

    @Override
    public void savePasswordResetToken(PasswordResetToken token) {
        try {
            DBUtils.saveEntity(databaseManager, token);
        } catch (ConstraintViolationException e) {
            e.printStackTrace(); // TODO: Handle duplicate reset token
        }
    }

    @Override
    public void logout(UserAuthenticationToken token) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(token);
        session.getTransaction().commit();
    }

    @Override
    public Optional<UserAuthenticationToken> getAuthToken(String token) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final Object obj = session.createQuery("FROM UserAuthenticationToken u WHERE u.value = :value")
                .setParameter("value", token)
                .uniqueResult();
        session.getTransaction().commit();
        if(obj instanceof UserAuthenticationToken)
            return Optional.of((UserAuthenticationToken) obj);
        else
            return Optional.empty();
    }

    @Override
    public Optional<PasswordResetToken> getPasswordResetToken(String token) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final Object obj = session.createQuery("FROM PasswordResetToken u WHERE u.value = :value")
                .setParameter("value", token)
                .uniqueResult();
        session.getTransaction().commit();
        if(obj instanceof PasswordResetToken)
            return Optional.of((PasswordResetToken) obj);
        else
            return Optional.empty();
    }


}
