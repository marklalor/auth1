package org.auth1.auth1.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

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
