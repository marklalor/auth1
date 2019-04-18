package org.auth1.auth1.core.authentication;

/**
 * <p>Result of resetting a password using a {@link org.auth1.auth1.model.entities.PasswordResetToken}.
 * See {@link AuthenticationManager#resetPassword(String, String)} for usage.</p>
 *
 * <p>See each enum constant for details.</p>
 */
public enum ResetPasswordResult {
    /**
     * The token was valid and the old password was replaced with the new password.
     */
    SUCCESS,
    /**
     * The token was invalid so the password was not reset.
     */
    INVALID_TOKEN,
    /**
     * The new password was invalid as it did not follow the password rules for this Auth1 instance.
     */
    INVALID_PASSWORD,
    /**
     * The user does not exist
     */
    USER_DOES_NOT_EXIST
}