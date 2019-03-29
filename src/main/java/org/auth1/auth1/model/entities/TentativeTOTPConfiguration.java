package org.auth1.auth1.model.entities;

import javax.persistence.*;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TentativeTOTPConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public TentativeTOTPConfiguration(int userId, byte[] tentativeTOTPSecret, ZonedDateTime expirationTime) {
        this.userId = userId;
        this.tentativeTOTPSecret = tentativeTOTPSecret;
        this.expirationTime = expirationTime;
    }

    public final static int VALIDITY_DURATION = 10;
    public final static TimeUnit VALIDITY_DURATION_UNITS = TimeUnit.MINUTES;

    public static TentativeTOTPConfiguration forUser(int userId) {
        final byte[] secret = new byte[80];
        new SecureRandom().nextBytes(secret);
        final ZonedDateTime expirationTime = ZonedDateTime.now().plus(VALIDITY_DURATION, VALIDITY_DURATION_UNITS.toChronoUnit());
        return new TentativeTOTPConfiguration(userId, secret, expirationTime);
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
