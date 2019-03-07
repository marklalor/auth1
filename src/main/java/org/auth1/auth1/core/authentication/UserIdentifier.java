package org.auth1.auth1.core.authentication;

import org.auth1.auth1.dao.UserDao;
import org.auth1.auth1.model.entities.User;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class UserIdentifier {
    public enum Type {
        USERNAME,
        EMAIL,
        USERNAME_OR_EMAIL
    }

    private final Type type;
    private final String value;

    public UserIdentifier(Type type, String value) {
        this.type = type;
        this.value = value;
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
    public UserIdentifier(@Nullable String username, @Nullable String email, @Nullable String usernameOrEmail) {
        if (Stream.of(username, email, usernameOrEmail).filter(Objects::nonNull).count() > 1) {
            throw new IllegalArgumentException("Only one of username, email, or usernameOrEmail may be non-null.");
        }

        if (username != null) {
            this.type = Type.USERNAME;
            this.value = username;
        } else if (email != null) {
            this.type = Type.EMAIL;
            this.value = email;
        } else {
            this.type = Type.USERNAME_OR_EMAIL;
            this.value = usernameOrEmail;
        }
    }

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
}
