package org.auth1.auth1.core.authentication;

import javax.annotation.Nullable;

public enum RegistrationResult {
    SUCCESS,
    SUCCESS_CONFIRM_EMAIL,
    USERNAME_DUPLICATE,
    EMAIL_DUPLICATE,
    EMAIL_REQUIRED,
    PASSWORD_WEAK,
    SERVICE_UNAVAILABLE
}