package org.auth1.auth1.dao;

import org.auth1.auth1.core.authentication.AuthenticationToken;
import org.auth1.auth1.model.entities.UserAuthenticationToken;

import java.util.Optional;

public interface TokenDao {

    void saveLoginToken(final UserAuthenticationToken token);

    Optional<UserAuthenticationToken> getToken(String token);
}
