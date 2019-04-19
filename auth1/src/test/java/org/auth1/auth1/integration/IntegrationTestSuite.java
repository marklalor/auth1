package org.auth1.auth1.integration;

import org.auth1.auth1.database.DatabaseLoader;
import org.auth1.auth1.database.RedisLoader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    LoginIntegrationTest.class,
    PasswordResetIntegrationTest.class,
    RegisterIntegrationTest.class
})
public class IntegrationTestSuite {

  private static DatabaseLoader databaseLoader;
  private static RedisLoader redisLoader;

  @BeforeClass
  public static void setupDatabase() throws Exception {
    databaseLoader = new DatabaseLoader();
    redisLoader = new RedisLoader();
    databaseLoader.startDB();
    redisLoader.startRedis();
  }

  @AfterClass
  public static void teardownDatabase() {
    databaseLoader.closeDB();
    redisLoader.closeRedis();
  }
}
