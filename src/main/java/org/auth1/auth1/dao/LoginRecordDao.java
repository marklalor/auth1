package org.auth1.auth1.dao;

import org.auth1.auth1.model.entities.LoginRecord;
import org.auth1.auth1.model.entities.PasswordResetToken;
import org.auth1.auth1.model.entities.UserAuthenticationToken;

import java.util.Optional;

public interface LoginRecordDao {

    void saveLoginRecord(final LoginRecord record);

}
