package org.auth1.auth1.dao;

import org.auth1.auth1.model.entities.TentativeTOTPConfiguration;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.Optional;

public abstract class UnimplementedTentativeTOTPConfigurationDao implements TentativeTOTPConfigurationDao {

    @Override
    public Optional<TentativeTOTPConfiguration> getConfiguration(int userId) {
        throw new NotYetImplementedException();
    }

    @Override
    public void saveConfiguration(TentativeTOTPConfiguration configuration) {
        throw new NotYetImplementedException();
    }
}
