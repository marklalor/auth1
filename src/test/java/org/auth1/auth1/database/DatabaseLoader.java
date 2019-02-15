package org.auth1.auth1.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_6_23;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseLoader {

    private static final String user = "root";
    private static final String password = "";
    private static final int port = 2215;
    private EmbeddedMysql mysqld;

    @Before
    public void startDB() {
        final MysqldConfig config = aMysqldConfig(v5_6_23)
                .withCharset(UTF8)
                .withPort(port)
                .withTimeout(2, TimeUnit.MINUTES)
                .build();
        mysqld = anEmbeddedMysql(config)
                .addSchema("aschema", classPathScript("db/001_init.sql"))
                .start();
    }

    @Test
    public void getFamilyMembers() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setDatabaseName("testDB");
        dataSource.setPort(port);

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM family;")) {
            rs.next();
            assertEquals("Brian", rs.getString("name"));
            rs.next();
            assertEquals("Mark", rs.getString("name"));
            rs.next();
            assertEquals("Yidi", rs.getString("name"));
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @After
    public void closeDB() {
        mysqld.stop();
    }
}
