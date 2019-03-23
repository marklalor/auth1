package org.auth1.auth1.core.authentication;

/**
 * <p>The result of calling the {@link AuthenticationManager#register(String, String, String)} method.</p>
 *
 * <p>See the enum constants for more details.</p>
 */
public enum RegistrationResult {
    /**
     * The account was successfully created and is automatically verified.
     */
    SUCCESS,
    /**
     * The account was successfully created, but is not verified.
     */
    SUCCESS_CONFIRM_EMAIL,
    /**
     * The account was not created, because an account with this username already exists.
     */
    USERNAME_DUPLICATE,
    /**
     * The account was not created, because an account with this email already exists.
     */
    EMAIL_DUPLICATE,
    /**
     * The account was not created, because an email is required for registration and one was not provided.
     */
    EMAIL_REQUIRED,
    /**
     * The account was not created, because a username is required for registration and one was not provided.
     */
    USERNAME_REQUIRED,
    /**
     * The account was not created, because the password was deemed too weak for account creation.
     */
    PASSWORD_WEAK,
    SERVICE_UNAVAILABLE
}