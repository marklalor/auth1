package org.auth1.auth1.util;

import org.auth1.auth1.model.DatabaseManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class DBUtil {
    public static <T> Optional<T> getFirstRow(DatabaseManager databaseManager, Class entityClass, String criteriaName, String criteriaValue) {
        final SessionFactory sessionFactory = databaseManager.getSessionFactory();
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        final CriteriaBuilder builder = session.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        final Root<T> root = criteria.from(entityClass);
        criteria.select(root);
        criteria.where(builder.equal(root.get(criteriaName), criteriaValue));
        final Query<T> query = session.createQuery(criteria);
        Optional<T> res = query.getResultList().stream().findFirst();
        session.getTransaction().commit();
        return res;
    }
}
