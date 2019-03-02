package org.auth1.auth1.core;

import org.auth1.auth1.model.Auth1Configuration;
import org.auth1.auth1.model.DatabaseConfiguration;
import org.auth1.auth1.model.DatabaseManager;
import org.springframework.stereotype.Component;


@Component
public class Auth1 {

    private final Auth1Configuration auth1Configuration;
    private final DatabaseManager databaseManager;

    public Auth1(Auth1Configuration auth1Configuration, DatabaseConfiguration databaseConfiguration) {
        this.auth1Configuration = auth1Configuration;
        this.databaseManager = new DatabaseManager(databaseConfiguration);
        this.databaseManager.initializeDatabase();
    }

    public Auth1Configuration getAuth1Configuration() {
        return auth1Configuration;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
