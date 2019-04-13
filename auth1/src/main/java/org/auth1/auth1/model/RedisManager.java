package org.auth1.auth1.model;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisManager {

    private final Jedis jedis;

    public RedisManager(RedisConfiguration redisConfiguration) {
        jedis = new Jedis(redisConfiguration.getIp(), redisConfiguration.getPort());
    }

    public Jedis getJedis() {
        return jedis;
    }
}
