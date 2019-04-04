package org.auth1.auth1.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "PasswordlessLoginToken")
public class PasswordlessLoginToken extends UserToken {

    public PasswordlessLoginToken(String value, int userId, ZonedDateTime issueTime, ZonedDateTime expirationTime) {
        super(value, userId, issueTime, expirationTime);
    }
}
