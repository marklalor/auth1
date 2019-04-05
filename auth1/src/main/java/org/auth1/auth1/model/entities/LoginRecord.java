package org.auth1.auth1.model.entities;

import org.auth1.auth1.core.authentication.AuthenticationResult;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.ZonedDateTime;

@Entity
@Table(name = "LoginRecord")
public class LoginRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "time")
    private ZonedDateTime time;


    @Column(name = "user_ip")
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    private String userIp;

    @Column(name = "user_agent")
    private String userAgent;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private AuthenticationResult.ResultType result;

    public LoginRecord(String username, ZonedDateTime time, String userIp, String userAgent, AuthenticationResult.ResultType result) {
        this.username = username;
        this.time = time;
        this.userIp = userIp;
        this.userAgent = userAgent;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public String getUserIp() {
        return userIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public AuthenticationResult.ResultType getResult() {
        return result;
    }
}
