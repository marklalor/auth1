package org.auth1.auth1.dao;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.LoginRecord;
import org.auth1.auth1.util.DBUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRecordDaoImpl implements LoginRecordDao {

    private final DatabaseManager databaseManager;

    public LoginRecordDaoImpl(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void saveLoginRecord(LoginRecord record) {
        try {
            DBUtils.saveEntity(databaseManager, record);
        } catch (ConstraintViolationException e) {
            e.printStackTrace(); // TODO: Handle duplicate login record
        }
    }
}
