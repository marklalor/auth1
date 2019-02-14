package org.auth1.auth1.model.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PasswordlessLoginToken")
public class PasswordlessLoginToken extends UserToken {

}
