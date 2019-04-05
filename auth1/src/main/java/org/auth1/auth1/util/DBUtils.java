package org.auth1.auth1.util;

import org.auth1.auth1.model.DatabaseManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DBUtils {
    public static void saveEntity(final DatabaseManager databaseManager, Object entityObj) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(entityObj);
        session.getTransaction().commit();
    }
}
