package org.auth1.auth1.model.entities;

import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@MappedSuperclass
public class UserToken {
    @Id
    @Column(name = "value")
    private String value;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "issue_time")
    private ZonedDateTime issueTime;

    @Column(name = "expiration_time")
    private ZonedDateTime expirationTime;

    public UserToken(String value, int userId, ZonedDateTime issueTime, ZonedDateTime expirationTime) {
        this.value = value;
        this.userId = userId;
        this.issueTime = issueTime;
        this.expirationTime = expirationTime;
    }

    public String getValue() {
        return value;
    }

    public int getUserId() {
        return userId;
    }

    public ZonedDateTime getIssueTime() {
        return issueTime;
    }

    public ZonedDateTime getExpirationTime() {
        return expirationTime;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "value='" + value + '\'' +
                ", userId=" + userId +
                ", issueTime=" + issueTime +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
