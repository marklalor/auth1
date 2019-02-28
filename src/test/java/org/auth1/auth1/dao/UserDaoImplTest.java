package org.auth1.auth1.dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.auth1.auth1.database.DatabaseLoader;
import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDaoImplTest {

    private static final int id = 1;
    private static final String username = "user";
    private static String password = "pass";
    private static byte[] totpSecret = new byte[128];
    private static String email = "email@email";
    private static boolean verified = false;
    private static boolean locked = false;
    private static ZonedDateTime creationTime = ZonedDateTime.now();

    private DatabaseLoader databaseLoader;
    private UserDao userDao;
    private User exampleUser;

    @BeforeEach
    void setUp() throws Exception {
        databaseLoader = new DatabaseLoader();
        databaseLoader.startDB();

        userDao = new UserDaoImpl(new DatabaseManager(DatabaseLoader.getDatabaseConfiguration()));
        exampleUser = new User(username, password, totpSecret, email, verified, locked, creationTime);
    }

    @AfterEach
    void tearDown() {
        databaseLoader.closeDB();
    }

    @Test
    void register() throws SQLException {
        userDao.register(exampleUser);
        final MysqlDataSource dataSource = DatabaseLoader.getMySqlDataSource();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM User;")) {
            rs.next();
            assertEquals(id, rs.getInt("id"));
            assertEquals(username, rs.getString("username"));
            assertEquals(password, rs.getString("password"));
            assertArrayEquals(totpSecret, rs.getBytes("totp_secret"));
            assertEquals(email, rs.getString("email"));
            assertEquals(verified, rs.getBoolean("verified"));
            assertEquals(locked, rs.getBoolean("locked"));
            assertTrue(creationTime.toLocalDate().isEqual(rs.getDate("creation_time").toLocalDate()));
        } catch (SQLException e) {
            throw e;
        }
    }

    @Test
    void login() throws SQLException {
        userDao.register(exampleUser);
        assertTrue(userDao.login(username, password));
    }
}