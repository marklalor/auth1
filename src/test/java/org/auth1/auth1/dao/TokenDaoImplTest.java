package org.auth1.auth1.dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.auth1.auth1.database.DatabaseLoader;
import org.auth1.auth1.err.UserDoesNotExistException;
import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.model.entities.UserAuthenticationToken;
import org.auth1.auth1.test_entities.ExampleUser;
import org.auth1.auth1.test_entities.ExampleUserAuthenticationToken;
import org.hibernate.criterion.Example;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenDaoImplTest {

    private DatabaseLoader databaseLoader;
    private TokenDao tokenDao;
    private UserDao userDao;

    @BeforeAll
    void setUp() throws Exception {
        databaseLoader = new DatabaseLoader();
        databaseLoader.startDB();

        final DatabaseManager databaseManager = new DatabaseManager(DatabaseLoader.getDatabaseConfiguration());
        tokenDao = new TokenDaoImpl(databaseManager);
        userDao = new UserDaoImpl(databaseManager);
    }

    @AfterAll
    void tearDown() {
        databaseLoader.closeDB();
    }

    @BeforeEach
    void deleteUserTable() throws SQLException {
        final MysqlDataSource dataSource = DatabaseLoader.getMySqlDataSource();
        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM UserAuthenticationToken");
        }
    }

    @Test
    void saveLoginToken() throws SQLException {
        userDao.saveUser(ExampleUser.INSTANCE);
        final UserAuthenticationToken exampleToken = ExampleUserAuthenticationToken.INSTANCE;
        tokenDao.saveLoginToken(exampleToken);
        final MysqlDataSource dataSource = DatabaseLoader.getMySqlDataSource();
        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM UserAuthenticationToken;");
            rs.next();
            assertEquals(ExampleUserAuthenticationToken.VALUE, rs.getString("value"));
            assertEquals(ExampleUserAuthenticationToken.USER_ID, rs.getInt("user_id"));
            assertEquals(ExampleUserAuthenticationToken.ISSUED_IP, rs.getString("issued_ip"));
            assertEquals(ExampleUserAuthenticationToken.ISSUED_USER_AGENT, rs.getString("issued_user_agent"));
        }
    }

    @Test
    void savePasswordResetToken() {
    }

    @Test
    void logout() {
        userDao.saveUser(ExampleUser.INSTANCE);
        final UserAuthenticationToken exampleToken = ExampleUserAuthenticationToken.INSTANCE;
        tokenDao.saveLoginToken(exampleToken);
        final Optional<UserAuthenticationToken> res = tokenDao.getAuthToken(ExampleUserAuthenticationToken.VALUE);
        assertTrue(res.isPresent());
        tokenDao.logout(exampleToken);
        assertTrue(res.isEmpty());
    }

    @Test
    void getAuthToken() {
    }

    @Test
    void getPasswordResetToken() {
    }
}