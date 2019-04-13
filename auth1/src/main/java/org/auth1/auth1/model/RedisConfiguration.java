package org.auth1.auth1.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisConfiguration {
    /**
     * Configuration for database connection parameters. May be
     * loaded from database.cnf with {@link #fromConfigurationFile(InputStream)}.
     */

    private final String ip;
    private final int port;
    private final int requestsAllowedPerMinute;

    public RedisConfiguration(String ip, int port, int requestsAllowedPerMinute) {
        this.ip = ip;
        this.port = port;
        this.requestsAllowedPerMinute = requestsAllowedPerMinute;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getRequestsAllowedPerMinute() {
        return requestsAllowedPerMinute;
    }

    /**
     * Load a full {@link RedisConfiguration} from the given input. This method checks the validity of the file and throws
     * an InvalidConfigurationException with a detailed error method if the input is semantically invalid or malformed.
     *
     * @param input a stream containing the data for the properties file.
     * @return a new {@link RedisConfiguration} loaded with the auth1 configuration parameters.
     * @throws IOException if the input stream could not be loaded.
     */
    public static RedisConfiguration fromConfigurationFile(InputStream input) throws IOException, Configuration.InvalidConfigurationException {
        var properties = new Properties();
        properties.load(input);

        final var ip = Configuration.getRequiredProperty(properties, "IP");
        final var port = Integer.valueOf(Configuration.getRequiredProperty(properties, "PORT"));
        final var requestsAllowedPerMinute = Integer.valueOf(Configuration.getRequiredProperty(properties, "REQUESTS_ALLOWED_PER_MINUTE"));

        return new RedisConfiguration(ip, port, requestsAllowedPerMinute);
    }

}
