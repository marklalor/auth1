package org.auth1.auth1.model.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import java.util.Arrays;

public class TentativeTOTPConfiguration {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "tentative_totp_secret")
    private byte[] tentativeTOTPSecret;

    @Column(name = "expiration_time")
    private ZonedDateTime expirationTime;

    public TentativeTOTPConfiguration() {

    }

    public TentativeTOTPConfiguration(int id, int userId, byte[] tentativeTOTPSecret, ZonedDateTime expirationTime) {
        this.id = id;
        this.userId = userId;
        this.tentativeTOTPSecret = tentativeTOTPSecret;
        this.expirationTime = expirationTime;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public byte[] getTentativeTOTPSecret() {
        return tentativeTOTPSecret;
    }

    public ZonedDateTime getExpirationTime() {
        return expirationTime;
    }

    @Override
    public String toString() {
        return "TentativeTOTPConfiguration{" +
                "id=" + id +
                ", userId=" + userId +
                ", tentativeTOTPSecret=" + Arrays.toString(tentativeTOTPSecret) +
                ", expirationTime=" + expirationTime +
                '}';
    }
}
