package org.auth1.auth1.model.entities;

import javax.persistence.*;
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

}
