package org.auth1.auth1.core.throttling;

import org.auth1.auth1.model.RedisConfiguration;
import org.auth1.auth1.model.RedisManager;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class AuthenticationThrottler {

    private final Jedis jedis;
    private final int requestsAllowedPerMinute;

    public AuthenticationThrottler(RedisManager redisManager, RedisConfiguration redisConfiguration) {
        this.jedis = redisManager.getJedis();
        this.requestsAllowedPerMinute = redisConfiguration.getRequestsAllowedPerMinute();
    }

    public boolean loginAllowed(String username) {
        jedis.incr(username);
        jedis.expire(username, 60);
        int numLogins = Integer.parseInt(jedis.get(username));
        return numLogins <= requestsAllowedPerMinute;
    }
}
