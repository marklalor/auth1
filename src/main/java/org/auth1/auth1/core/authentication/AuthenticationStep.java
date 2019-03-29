package org.auth1.auth1.core.authentication;

import org.auth1.auth1.model.entities.User;

import javax.annotation.Nullable;

/**
 * <p>Represents a step in the authentication sequence,
 * for example checking password, checking rate limiting,
 * etc, based on the given user, password, and totpCode</p>
 *
 * <p>Should return {@link AuthenticationStepResult#passed()} if the step succeeded,
 * otherwise should return a result generated from {@link AuthenticationStepResult#of(AuthenticationResult)}</p>
 */
public interface AuthenticationStep {
    AuthenticationStepResult doStep(User user, final String password, final @Nullable String totpCode);
}
