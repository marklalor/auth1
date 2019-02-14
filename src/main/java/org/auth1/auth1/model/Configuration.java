package org.auth1.auth1.model;

import java.util.Properties;

public class Configuration {

    public static class InvalidConfigurationException extends Exception {

        public InvalidConfigurationException(String message) {
            super(message);
        }

    }

    /**
     * Helper method to throw a more informative error message if the
     * given properties file is missing the specified property.
     */
    static String getRequiredProperty(Properties properties, String key) throws InvalidConfigurationException {
        var property = properties.getProperty(key);
        if (property == null) {
            throw new InvalidConfigurationException(String.format("Auth1Configuration file missing required key \"%s\"",
                    key));
        }
        return property;
    }
}
