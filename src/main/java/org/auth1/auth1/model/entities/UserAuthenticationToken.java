package org.auth1.auth1.model.entities;

import org.auth1.auth1.core.authentication.AuthenticationUtils;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "UserAuthenticationToken")
public class UserAuthenticationToken extends UserToken {

    @Column(name = "issued_ip")
    private String issuedIp;

    @Column(name = "issued_user_agent")
    private String issuedUserAgent;

    public UserAuthenticationToken(String value, int userId, ZonedDateTime issueTime, ZonedDateTime expirationTime, String issuedIp, String issuedUserAgent) {
        super(value, userId, issueTime, expirationTime);
        this.issuedIp = issuedIp;
        this.issuedUserAgent = issuedUserAgent;
    }

    public static UserAuthenticationToken withDuration(int userId, long time, TimeUnit unit) {
        final ZonedDateTime issueTime = ZonedDateTime.now();
        final ZonedDateTime expirationTime = issueTime.plusSeconds(unit.toSeconds(time));
        final String value = AuthenticationUtils.generateToken(128, Base64Utils::encodeToString);
        return new UserAuthenticationToken(value, userId, issueTime, expirationTime, null, null);
    }

    public String getIssuedIp() {
        return issuedIp;
    }

    public String getIssuedUserAgent() {
        return issuedUserAgent;
    }

    @Override
    public String toString() {
        return "UserAuthenticationToken{" +
                "issuedIp='" + issuedIp + '\'' +
                ", issuedUserAgent='" + issuedUserAgent + '\'' +
                '}';
    }
}
