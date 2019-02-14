package org.auth1.auth1.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "UserVerificationToken")
public class UserVerificationToken extends UserToken {

}
