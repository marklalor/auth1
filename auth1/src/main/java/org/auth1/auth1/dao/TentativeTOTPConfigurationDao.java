package org.auth1.auth1.dao;

import org.auth1.auth1.model.entities.TentativeTOTPConfiguration;

import java.util.Optional;

public interface TentativeTOTPConfigurationDao {

    // There should only exist one configuration in the db *per user* at a time!

    Optional<TentativeTOTPConfiguration> getConfiguration(int userId);

    void saveConfiguration(TentativeTOTPConfiguration configuration);
}
