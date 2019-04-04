package org.auth1.auth1;

import org.auth1.auth1.model.Auth1Configuration;
import org.auth1.auth1.model.Configuration;
import org.auth1.auth1.model.DatabaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public DatabaseConfiguration databaseConfiguration(@Value("${config.databaseConfigurationPath}")
                                                               String databaseConfigurationPath) {
        try {
            return
                    DatabaseConfiguration.fromConfigurationFile(new FileInputStream(databaseConfigurationPath));
        } catch (IOException | Configuration.InvalidConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    @Bean
    public Auth1Configuration auth1Configuration(@Value("${config.auth1ConfigurationPath}")
                                                         String auth1ConfigurationPath) {
        try {
            return
                    Auth1Configuration.fromConfigurationFile(new FileInputStream(auth1ConfigurationPath));
        } catch (IOException | Configuration.InvalidConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
