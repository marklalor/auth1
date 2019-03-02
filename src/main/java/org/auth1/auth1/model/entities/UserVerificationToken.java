package org.auth1.auth1.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "UserVerificationToken")
public class UserVerificationToken extends UserToken {

    public UserVerificationToken(String value, int userId, ZonedDateTime issueTime, ZonedDateTime expirationTime) {
        super(value, userId, issueTime, expirationTime);
    }
}
