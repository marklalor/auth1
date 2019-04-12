package org.auth1.auth1.dao;

import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.LoginRecord;
import org.auth1.auth1.util.DBUtils;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRecordDaoImpl implements LoginRecordDao {

    private final DatabaseManager databaseManager;

    public LoginRecordDaoImpl(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void saveLoginRecord(LoginRecord record) {
        DBUtils.saveEntity(databaseManager, record);
    }
}
