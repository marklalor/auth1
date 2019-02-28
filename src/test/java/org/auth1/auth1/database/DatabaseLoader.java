package org.auth1.auth1.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.auth1.auth1.model.DatabaseConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_6_23;

public class DatabaseLoader {

    private static final String IP = "localhost";
    private static final int PORT = 3307;
    private static final String DATABASE = "auth1";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private EmbeddedMysql mysqld;

    public void startDB() {
        final MysqldConfig config = aMysqldConfig(v5_6_23)
                .withCharset(UTF8)
                .withPort(PORT)
                .withTimeout(2, TimeUnit.MINUTES)
                .build();
        mysqld = anEmbeddedMysql(config)
                .addSchema("auth1", classPathScript("initialize.sql"))
                .start();
    }

    public static DatabaseConfiguration getDatabaseConfiguration() throws UnknownHostException {
        return new DatabaseConfiguration(InetAddress.getByName(IP),
                PORT,
                DATABASE,
                USERNAME,
                PASSWORD);
    }

    public static MysqlDataSource getMySqlDataSource() {
        final MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setDatabaseName(DATABASE);
        dataSource.setPort(PORT);
        return dataSource;
    }

    public void closeDB() {
        mysqld.stop();
    }
}
