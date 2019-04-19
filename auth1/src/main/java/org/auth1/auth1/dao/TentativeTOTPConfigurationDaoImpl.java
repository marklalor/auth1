package org.auth1.auth1.dao;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.TentativeTOTPConfiguration;
import org.auth1.auth1.model.entities.UserAuthenticationToken;
import org.auth1.auth1.util.DBUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import javax.validation.Constraint;
import java.util.Optional;

@Repository
public class TentativeTOTPConfigurationDaoImpl implements TentativeTOTPConfigurationDao {

    private final DatabaseManager databaseManager;

    public TentativeTOTPConfigurationDaoImpl(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Optional<TentativeTOTPConfiguration> getConfiguration(int userId) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final Object obj = session.createQuery("FROM TentativeTOTPConfiguration c WHERE c.userId = :value")
                .setParameter("value", userId)
                .uniqueResult();
        session.getTransaction().commit();
        if(obj instanceof TentativeTOTPConfiguration)
            return Optional.of((TentativeTOTPConfiguration) obj);
        else
            return Optional.empty();
    }

    @Override
    public void saveConfiguration(TentativeTOTPConfiguration configuration) {
        try {
            DBUtils.saveEntity(databaseManager, configuration);
        } catch (ConstraintViolationException e) {
            e.printStackTrace(); // TODO: Handle duplicate TOTP config
        }
    }
}
