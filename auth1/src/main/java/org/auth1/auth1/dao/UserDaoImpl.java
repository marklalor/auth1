package org.auth1.auth1.dao;

import org.auth1.auth1.core.authentication.UserIdentifier;
import org.auth1.auth1.err.EmailAlreadyExistsException;
import org.auth1.auth1.err.UsernameAlreadyExistsException;
import org.auth1.auth1.err.UserDoesNotExistException;
import org.auth1.auth1.model.ConstraintNames;
import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.util.DBUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final DatabaseManager databaseManager;

    public UserDaoImpl(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void saveUser(final User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        try {
            DBUtils.saveEntity(databaseManager, user);
        } catch (ConstraintViolationException e) {
            if (e.getConstraintName().equals(ConstraintNames.UNIQUE_USERNAME_CONSTRAINT)) {
                throw new UsernameAlreadyExistsException(user.getUsername());
            } else if (e.getConstraintName().equals(ConstraintNames.UNIQUE_EMAIL_CONSTRAINT)) {
                throw new EmailAlreadyExistsException(user.getEmail());
            }
        }
    }

    @Override
    public void updateUser(final User user) {
        DBUtils.saveOrUpdateEntity(databaseManager, user);
    }

    @Override
    public void lockUser(UserIdentifier userIdentifier) {
       setUserLockedStatus(userIdentifier, true);
    }

    @Override
    public void unlockUser(UserIdentifier userIdentifier) {
        setUserLockedStatus(userIdentifier, false);
    }

    private void setUserLockedStatus(UserIdentifier userIdentifier, boolean shouldLockUser) {
        Session session = databaseManager.getSessionFactory().openSession();
        session.beginTransaction();

        String updateQuery = "UPDATE User u SET u.locked = :locked WHERE u.:fieldName = :userId";
        session.createQuery( updateQuery )
                .setParameter( "fieldName", userIdentifier.getType().getFieldName() )
                .setParameter( "locked", shouldLockUser )
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void resetPassword(UserIdentifier userIdentifier, String password) throws UserDoesNotExistException {
        final Optional<User> res = userIdentifier.getUser(this);
        if (res.isPresent()){
            final User user = res.get();
            user.setPassword(password);
            final SessionFactory sessionFactory = databaseManager.getSessionFactory();
            final Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
        } else
            throw new UserDoesNotExistException(userIdentifier.getValue());
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
        session.beginTransaction();
        final String placeHolder = "placeholder";
        final Object obj = session.createQuery(String.format("FROM User u WHERE u.%s = :%s", columnName, placeHolder))
                .setParameter(placeHolder, columnValue)
                .uniqueResult();
        session.getTransaction().commit();
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
