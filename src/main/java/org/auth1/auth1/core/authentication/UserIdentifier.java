package org.auth1.auth1.core.authentication;

import org.auth1.auth1.dao.UserDao;
import org.auth1.auth1.model.entities.User;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * <p>Identifies an Auth1 user by either a username, an email,
 * or a string that may represent a username or email.</p>
 * To create an instance, see the methods {@link #forEmail(String)},
 * {@link #forUsername(String)}, {@link #forUsernameOrEmail(String)}, and {@link #forOneOf(String, String, String)}
 */
public class UserIdentifier {
    public enum Type {
        USERNAME,
        EMAIL,
        USERNAME_OR_EMAIL
    }

    private final Type type;
    private final String value;

    private UserIdentifier(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Returns a user identifier based on the given email address.
     * @param email the email that identifies a user.
     */
    public static UserIdentifier forEmail(String email) {
        return new UserIdentifier(Type.EMAIL, email);
    }

    /**
     * Returns a user identifier based on the given username.
     * @param username the username that identifies a user.
     */
    public static UserIdentifier forUsername(String username) {
        return new UserIdentifier(Type.USERNAME, username);
    }

    /**
     * Returns a user identifier based on a string that may represent a username or email address.
     * @param usernameOrEmail string that can be a username or email address. The application will first try
     *                        to treat the string as a username, then as an email if that fails.
     */
    public static UserIdentifier forUsernameOrEmail(String usernameOrEmail) {
        return new UserIdentifier(Type.USERNAME_OR_EMAIL, usernameOrEmail);
    }

    /**
     * <p>Returns a user-identifying object for the specified {@code username}, {@code email},
     * or {@code usernameOrEmail}, depending on which one is provided. Only one parameter may be non-null.</p>
     *
     * @param username the username of a user to look up.
     * @param email the email belonging to a user to look up.
     * @param usernameOrEmail a string representing either a username or an email of a user to look up.
     * @throws {@link IllegalArgumentException} if more than one parameter is non-null since
     * the semantics of the return value would be otherwise confusing.
     */
    public static UserIdentifier forOneOf(@Nullable String username, @Nullable String email, @Nullable String usernameOrEmail) {
        if (Stream.of(username, email, usernameOrEmail).filter(Objects::nonNull).count() > 1) {
            throw new IllegalArgumentException("Only one of username, email, or usernameOrEmail may be non-null.");
        }

        if (email != null) {
            return UserIdentifier.forEmail(email);
        } else if (username != null) {
            return UserIdentifier.forUsername(username);
        } else {
            return UserIdentifier.forUsernameOrEmail(usernameOrEmail);
        }
    }

    /**
     * Uses the provided {@link UserDao} to call the right DAO method based on
     * the backing of this {@link UserIdentifier}.
     * @return a {@link User} object if one exists and is associated with
     * his {@link UserIdentifier}, {@link Optional#empty()} otherwise.
     */
    public Optional<User> getUser(UserDao userDao) {
        switch (this.type) {
            case USERNAME:
                return userDao.getUserByUsername(value);
            case EMAIL:
                return userDao.getUserByEmail(value);
            case USERNAME_OR_EMAIL:
                return userDao.getUserByUsername(value).or(() -> userDao.getUserByEmail(value));
        }

        throw new RuntimeException("UserIdentifier.Type not handled.");
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this.type) {
            case USERNAME:
                return "UserIdentifier(username=\"" + this.value + "\")";
            case EMAIL:
                return "UserIdentifier(email=\"" + this.value + "\")";
            case USERNAME_OR_EMAIL:
                return "UserIdentifier(usernameOrEmail=\"" + this.value + "\")";
        }

        throw new IllegalStateException("Didn't cover all switch cases.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIdentifier that = (UserIdentifier) o;
        return type == that.type &&
                com.google.common.base.Objects.equal(value, that.value);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(type, value);
    }
}
