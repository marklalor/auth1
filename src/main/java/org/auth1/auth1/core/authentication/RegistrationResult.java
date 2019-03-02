package org.auth1.auth1.core.authentication;

public enum RegistrationResult {
    SUCCESS,
    SUCCESS_CONFIRM_EMAIL,
    USERNAME_DUPLICATE,
    EMAIL_DUPLICATE,
    EMAIL_REQUIRED,
    USERNAME_REQUIRED,
    PASSWORD_WEAK,
    SERVICE_UNAVAILABLE
}