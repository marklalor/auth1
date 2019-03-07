package org.auth1.auth1.model.entities;

import org.auth1.auth1.core.authentication.AuthenticationUtils;
import org.springframework.util.Base64Utils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "PasswordResetToken")
public class PasswordResetToken extends UserToken {

    public PasswordResetToken(String value, int userId, ZonedDateTime issueTime, ZonedDateTime expirationTime) {
        super(value, userId, issueTime, expirationTime);
    }

    public static PasswordResetToken withDuration(int userId, long time, TimeUnit unit) {
        final ZonedDateTime issueTime = ZonedDateTime.now();
        final ZonedDateTime expirationTime = issueTime.plusSeconds(unit.toSeconds(time));
        final String value = AuthenticationUtils.generateToken(128, Base64Utils::encodeToString);
        return new PasswordResetToken(value, userId, issueTime, expirationTime);
    }
}
