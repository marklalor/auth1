package org.auth1.auth1.model;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.auth1.auth1.model.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class DatabaseManager {
    private final DatabaseConfiguration configuration;
    private final SessionFactory sessionFactory;

    public DatabaseManager(DatabaseConfiguration configuration) {
        System.out.println("Hello world");
        System.out.println("Config: " + configuration.toString());
        this.configuration = configuration;
        this.sessionFactory = initializeHibernate(configuration);
    }

    private static SessionFactory initializeHibernate(DatabaseConfiguration configuration) {
        org.hibernate.cfg.Configuration hibernateConfiguration = new org.hibernate.cfg.Configuration();
        hibernateConfiguration.configure("hibernate.cfg.xml");
        hibernateConfiguration.getProperties().setProperty("hibernate.connection.url", configuration.getURL());
        hibernateConfiguration.getProperties().setProperty("hibernate.connection.username", configuration.getUsername());
        hibernateConfiguration.getProperties().setProperty("hibernate.connection.password", configuration.getPassword());

        hibernateConfiguration.addAnnotatedClass(LoginRecord.class);
        hibernateConfiguration.addAnnotatedClass(PasswordlessLoginToken.class);
        hibernateConfiguration.addAnnotatedClass(PasswordResetToken.class);
        hibernateConfiguration.addAnnotatedClass(User.class);
        hibernateConfiguration.addAnnotatedClass(UserToken.class);
        hibernateConfiguration.addAnnotatedClass(UserVerificationToken.class);
        hibernateConfiguration.addAnnotatedClass(UserAuthenticationToken.class);
        hibernateConfiguration.addAnnotatedClass(TentativeTOTPConfiguration.class);

        return hibernateConfiguration.buildSessionFactory();
    }

    public void initializeDatabase() {
        try {
            var sql = Resources.toString(Resources.getResource("initialize.sql"), Charsets.UTF_8);
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            // These Hibernate SQL queries have to be one statement each... splitting them.
            Arrays.stream(sql.split(";")).map(session::createSQLQuery).forEach(NativeQuery::executeUpdate);
            session.getTransaction().commit();
            session.close();
        } catch (IOException e) {
            System.err.print("Could not load database initialization file... shutting down.");
            System.exit(1);
        }
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }
}
