package org.auth1.auth1.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "LoginToken")
public class LoginToken extends UserToken {

    @Column(name = "issued_ip")
    private String issuedIp;

    @Column(name = "issued_user_agent")
    private String issuedUserAgent;

}
