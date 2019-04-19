package org.auth1.auth1.dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.auth1.auth1.core.authentication.UserIdentifier;
import org.auth1.auth1.database.DatabaseLoader;
import org.auth1.auth1.err.EmailAlreadyExistsException;
import org.auth1.auth1.err.UserDoesNotExistException;
import org.auth1.auth1.err.UsernameAlreadyExistsException;
import org.auth1.auth1.model.DatabaseManager;
import org.auth1.auth1.model.entities.User;
import org.auth1.auth1.test_entities.ExampleUser;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoImplTest {

    private DatabaseLoader databaseLoader;
    private UserDao userDao;

    @BeforeAll
    void setUp() throws Exception {
        databaseLoader = new DatabaseLoader();
        databaseLoader.startDB();

        userDao = new UserDaoImpl(new DatabaseManager(DatabaseLoader.getDatabaseConfiguration()));
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
            stmt.executeUpdate("DELETE FROM User");
        }
    }

    @Test
    void register() throws SQLException, EmailAlreadyExistsException, UsernameAlreadyExistsException {
        userDao.saveUser(ExampleUser.INSTANCE);
        final MysqlDataSource dataSource = DatabaseLoader.getMySqlDataSource();
        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User;");
            rs.next();
            assertEquals(ExampleUser.USERNAME, rs.getString("username"));
            assertEquals(ExampleUser.PASSWORD, rs.getString("password"));
            assertArrayEquals(ExampleUser.TOTP_SECRET, rs.getBytes("totp_secret"));
            assertEquals(ExampleUser.EMAIL, rs.getString("email"));
            assertEquals(ExampleUser.VERIFIED, rs.getBoolean("verified"));
            assertEquals(ExampleUser.LOCKED, rs.getBoolean("locked"));
            assertTrue(ExampleUser.CREATION_TIME.toLocalDate().isEqual(rs.getDate("creation_time").toLocalDate()));
        }
    }

    @Test
    void register_userExists() {
        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
            userDao.saveUser(ExampleUser.INSTANCE);
            userDao.saveUser(ExampleUser.INSTANCE);
        });
    }

    @Test
    void getUserByUsername_exists() throws EmailAlreadyExistsException, UsernameAlreadyExistsException {
        final User exampleUser = ExampleUser.INSTANCE;
        userDao.saveUser(exampleUser);
        final Optional<User> user = userDao.getUserByUsername(ExampleUser.USERNAME);
        assertTrue(user.isPresent());
        assertEquals(ExampleUser.INSTANCE, user.get());
    }

    @Test
    void getUserByUsername_notExists() {
        assertTrue(userDao.getUserByUsername(ExampleUser.USERNAME).isEmpty());
    }

    @Test
    void getUserByEmail_exists() throws EmailAlreadyExistsException, UsernameAlreadyExistsException {
        final User exampleUser = ExampleUser.INSTANCE;
        userDao.saveUser(exampleUser);
        final Optional<User> user = userDao.getUserByEmail(ExampleUser.EMAIL);
        assertTrue(user.isPresent());
        assertEquals(exampleUser, user.get());
    }

    @Test
    void getUserByEmail_notExists() {
        assertTrue(userDao.getUserByEmail(ExampleUser.EMAIL).isEmpty());
    }

    @Test
    void resetPassword_userExists() throws Exception {
        final User exampleUser = ExampleUser.INSTANCE;
        userDao.saveUser(exampleUser);
        Optional<User> res = userDao.getUserByUsername(exampleUser.getUsername());
        assertTrue(res.isPresent());
        assertEquals(ExampleUser.PASSWORD, res.get().getPassword());

        userDao.resetPassword(UserIdentifier.forUsername(exampleUser.getUsername()), ExampleUser.altPassword);
        res = userDao.getUserByUsername(exampleUser.getUsername());
        assertTrue(res.isPresent());
        assertEquals(ExampleUser.altPassword, res.get().getPassword());
    }

    @Test
    void resetPassword_userDoesNotExist() {
        final User exampleUser = ExampleUser.INSTANCE;
        assertThrows(UserDoesNotExistException.class, () -> userDao.resetPassword(UserIdentifier.forUsername(exampleUser.getUsername()), ExampleUser.altPassword));
    }
}