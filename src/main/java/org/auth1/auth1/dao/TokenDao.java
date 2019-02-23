package org.auth1.auth1.dao;

import org.auth1.auth1.model.entities.LoginToken;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.model.entities.UserToken;

import java.util.Optional;

public interface TokenDao {

    void saveLoginToken(final LoginToken token);

}
