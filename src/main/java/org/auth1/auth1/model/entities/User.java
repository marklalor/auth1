package org.auth1.auth1.model.entities;

import org.auth1.auth1.core.authentication.UserIdentifier;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "totp_secret")
    private byte[] totpSecret;

    @Column(name = "email")
    private String email;

    @Column(name = "verified")
    private boolean verified;

    @Column(name = "locked")
    private boolean locked;

    @Column(name = "creation_time")
    private ZonedDateTime creationTime;

    public User() {

    }

    public User(String username, String password, byte[] totpSecret, String email, boolean verified, boolean locked, ZonedDateTime creationTime) {
        this.username = username;
        this.password = password;
        this.totpSecret = totpSecret;
        this.email = email;
        this.verified = verified;
        this.locked = locked;
        this.creationTime = creationTime;
    }

    public UserIdentifier getAnyUserIdentifier() {
        return UserIdentifier.forOneOf(username, email, null);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getTotpSecret() {
        return totpSecret;
    }

    public String getEmail() {
        return email;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean isLocked() {
        return locked;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return verified == user.verified &&
                locked == user.locked &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, verified, locked);
    }

    /**
     * Don't use this in logs, just for debugging... has the hashed password.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", totpSecret=" + Arrays.toString(totpSecret) +
                ", email='" + email + '\'' +
                ", verified=" + verified +
                ", locked=" + locked +
                ", creationTime=" + creationTime +
                '}';
    }
}
