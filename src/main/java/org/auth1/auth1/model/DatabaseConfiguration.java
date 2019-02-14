package org.auth1.auth1.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

public class DatabaseConfiguration {

    private final InetAddress ip;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public DatabaseConfiguration(InetAddress ip, int port, String database, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getURL() {
        return String.format("jdbc:mysql://%s:%d/%s", ip.getHostName(), port, database);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Load a full {@link DatabaseConfiguration} from the given input. This method checks the validity of the file and throws
     * an InvalidConfigurationException with a detailed error method if the input is semantically invalid or malformed.
     *
     * @param input a stream containing the data for the properties file.
     * @return a new {@link DatabaseConfiguration} loaded with the auth1 configuration parameters.
     * @throws IOException if the input stream could not be loaded.
     */
    public static DatabaseConfiguration fromConfigurationFile(InputStream input) throws IOException, Configuration.InvalidConfigurationException {
        var properties = new Properties();
        properties.load(input);

        final var ip = InetAddress.getByName(Configuration.getRequiredProperty(properties, "IP"));
        final var port = Integer.valueOf(Configuration.getRequiredProperty(properties, "PORT"));
        final var database = Configuration.getRequiredProperty(properties, "DATABASE");
        final var username = Configuration.getRequiredProperty(properties, "USERNAME");
        final var password = Configuration.getRequiredProperty(properties, "PASSWORD");

        return new DatabaseConfiguration(ip, port, database, username, password);
    }
}
