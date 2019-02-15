package org.auth1.auth1.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

import static org.auth1.auth1.model.DatabaseConfiguration.JDBC_URL_FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatabaseConfigurationTest {
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOSTNAME = "hostname";
    private static final String DEFAULT_DATABASE = "database";
    private static final String DEFAULT_USERNAME = "username";
    private static final String DEFAULT_PASSWORD = "password";

    private DatabaseConfiguration databaseConfiguration;
    private InetAddress mockIp;
    private int port;
    private String database;
    private String username;
    private String password;

    @BeforeEach
    void setUp() {
        mockIp = mock(InetAddress.class);
        when(mockIp.getHostName()).thenReturn(DEFAULT_HOSTNAME);
        port = DEFAULT_PORT;
        database = DEFAULT_DATABASE;
        username = DEFAULT_USERNAME;
        password = DEFAULT_PASSWORD;
        databaseConfiguration = new DatabaseConfiguration(mockIp, port, database, username, password);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getIp() {
        assertEquals(mockIp, databaseConfiguration.getIp());
    }

    @Test
    void getPort() {
        assertEquals(port, databaseConfiguration.getPort());
    }

    @Test
    void getDatabase() {
        assertEquals(database, databaseConfiguration.getDatabase());
    }

    @Test
    void getURL() {
        assertEquals(String.format(JDBC_URL_FORMAT, DEFAULT_HOSTNAME, DEFAULT_PORT, DEFAULT_DATABASE), databaseConfiguration.getURL());
    }

    @Test
    void getUsername() {
        assertEquals(username, databaseConfiguration.getUsername());
    }

    @Test
    void getPassword() {
        assertEquals(password, databaseConfiguration.getPassword());
    }

}