package org.auth1.auth1.core.authentication;

public enum ConfirmTentativeTOTPResult {
    SUCCESS,
    TENTATIVE_TOTP_SECRET_NOT_CREATED,
    TENTATIVE_TOTP_SECRET_EXPIRED,
    INVALID_CODE,
    INVALID_TOKEN
}
