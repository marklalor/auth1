package org.auth1.auth1.dao;

import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.TentativeTOTPConfiguration;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TentativeTOTPConfigurationDaoImpl implements TentativeTOTPConfigurationDao {

    private final DatabaseManager databaseManager;

    public TentativeTOTPConfigurationDaoImpl(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public Optional<TentativeTOTPConfiguration> getConfiguration(int userId) {
        throw new NotYetImplementedException();
    }

    @Override
    public void saveConfiguration(TentativeTOTPConfiguration configuration) {
        throw new NotYetImplementedException();
    }
}
