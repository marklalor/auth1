package org.auth1.auth1.api.status;

import java.time.LocalDateTime;
import java.util.Collection;

public class Status {
    enum Feature {
        ENROLL,
        LOGIN_TOKEN,
        AUTH_TOKEN,
        MFA,
        RECOVER_PASSWORD
    }

    private final LocalDateTime currentTime;
    private final String name;
    private final Collection<Feature> availableFeatures;
    private final Collection<Feature> pendingFeatures;

    Status(String name, LocalDateTime currentTime, Collection<Feature> availableFeatures, Collection<Feature> pendingFeatures) {
        this.name = name;
        this.currentTime = currentTime;
        this.availableFeatures = availableFeatures;
        this.pendingFeatures = pendingFeatures;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public String getName() {
        return name;
    }

    public Collection<Feature> getAvailableFeatures() {
        return availableFeatures;
    }

    public Collection<Feature> getPendingFeatures() {
        return pendingFeatures;
    }
}
