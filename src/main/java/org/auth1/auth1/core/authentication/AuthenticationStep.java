package org.auth1.auth1.core.authentication;

import org.auth1.auth1.model.entities.User;

import javax.annotation.Nullable;

public interface AuthenticationStep {
    AuthenticationStepResult doStep(User user, final String password, final @Nullable String totpCode);
}
