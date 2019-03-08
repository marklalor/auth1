package org.auth1.auth1.dao;

import org.auth1.auth1.err.UserDoesNotExistException;
import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.util.DBUtils;
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
    public void saveUser(final User user) {
        DBUtils.saveEntity(databaseManager, user);
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
    public void resetPassword(String username, String password) throws UserDoesNotExistException {
        final Optional<User> res = getUserByUsername(username);
        if (res.isPresent()){
            final User user = res.get();
            user.setPassword(password);
            final SessionFactory sessionFactory = databaseManager.getSessionFactory();
            final Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
        } else
            throw new UserDoesNotExistException(username);
    }

    @Override
    public Optional<User> getUserById(int userId) {
        return getUserByColumnValue("id", userId);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return getUserByColumnValue("username", username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return getUserByColumnValue("email", email);
    }

    private Optional<User> getUserByColumnValue(String columnName, Object columnValue) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        final String placeHolder = "placeholder";
        final Object obj = session.createQuery(String.format("FROM User u WHERE u.%s = :%s", columnName, placeHolder))
                .setParameter(placeHolder, columnValue)
                .uniqueResult();
        if(obj instanceof User)
            return Optional.of((User)obj);
        else
            return Optional.empty();
    }

    @Override
    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail) {
        return this.getUserByUsername(usernameOrEmail).or(() -> this.getUserByEmail(usernameOrEmail));
    }
}
