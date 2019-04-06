package org.auth1.auth1.dao;

import org.auth1.auth1.model.entities.LoginRecord;
import org.hibernate.cfg.NotYetImplementedException;

public abstract class UnimplementedLoginRecordDao implements LoginRecordDao {
    @Override
    public void saveLoginRecord(LoginRecord record) {
        throw new NotYetImplementedException();
    }
}
