package org.auth1.auth1.core;

import org.auth1.auth1.model.Auth1Configuration;
import org.auth1.auth1.model.Configuration;
import org.auth1.auth1.model.DatabaseConfiguration;

import java.io.FileInputStream;
import java.io.IOException;

public class CLI {

    public static void main(String[] args) {
        var auth1ConfigurationPath = args[0];
        var databaseConfigurationPath = args[1];

        try {
            final Auth1Configuration auth1Configuration
                    = Auth1Configuration.fromConfigurationFile(new FileInputStream(auth1ConfigurationPath));
            final DatabaseConfiguration databaseConfiguration
                    = DatabaseConfiguration.fromConfigurationFile(new FileInputStream(databaseConfigurationPath));
            final var auth1Instance = new Auth1(auth1Configuration, databaseConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Configuration.InvalidConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
