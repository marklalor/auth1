package org.auth1.auth1.database;

import redis.embedded.RedisServer;

import java.io.IOException;

public class RedisLoader {

    private static final int REDIS_TEST_PORT = 6380;
    private RedisServer redisServer;
    public void startRedis() {
        try {
            redisServer = new RedisServer(6380);
        } catch (IOException e) {
            e.printStackTrace();
        }
        redisServer.start();
    }

    public void closeRedis() {
        redisServer.stop();
    }
}
