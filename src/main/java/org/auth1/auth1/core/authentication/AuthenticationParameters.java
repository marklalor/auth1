package org.auth1.auth1.core.authentication;

import com.google.common.base.Objects;

import javax.annotation.Nullable;
import java.util.Optional;

public class AuthenticationParameters {
    private final @Nullable String username;
    private final @Nullable String email;
    private final @Nullable String password;
    private final @Nullable String totpCode;

    public AuthenticationParameters(@Nullable String username,
                                    @Nullable String email,
                                    @Nullable String password,
                                    @Nullable String totpCode) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.totpCode = totpCode;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public Optional<String> getTotpCode() {
        return Optional.ofNullable(totpCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationParameters that = (AuthenticationParameters) o;
        return Objects.equal(username, that.username) &&
                Objects.equal(email, that.email) &&
                Objects.equal(password, that.password) &&
                Objects.equal(totpCode, that.totpCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, email, password, totpCode);
    }

    @Override
    public String toString() {
        return "AuthenticationParameters{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", totpCode='" + totpCode + '\'' +
                '}';
    }
}
