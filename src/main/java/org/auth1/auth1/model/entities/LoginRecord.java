package org.auth1.auth1.model.entities;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.ZonedDateTime;

@Entity
@Table(name = "LoginRecord")
public class LoginRecord {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "time")
    private ZonedDateTime time;

    @Column(name = "username")
    private String username;

    @Column(name = "user_ip")
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    private String userIp;

    @Column(name = "ser_agent")
    private String userAgent;

    @Column(name = "client_id")
    private String clientId;

}
