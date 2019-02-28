package org.auth1.auth1.dao;

import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final DatabaseManager databaseManager;

    public UserDaoImpl(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean login(String username, String password) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final User user = session.load(User.class, username);
        session.getTransaction().commit();
        return user.getPassword().equals(password);
    }

    public void register(final User user) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
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
    public Optional<User> getUserByUsername(String username) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final User user = session.get(User.class, username);
        session.getTransaction().commit();
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return getUserByUsername(email);
    }
}
