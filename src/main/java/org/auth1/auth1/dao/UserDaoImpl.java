package org.auth1.auth1.dao;

import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl {

    private final DatabaseManager databaseManager;

    public UserDaoImpl(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void register(final User user) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }
}
