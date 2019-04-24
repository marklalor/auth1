package org.auth1.auth1.core.throttling;

import org.auth1.auth1.core.authentication.UserIdentifier;
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

  public boolean loginAllowed(UserIdentifier userId) {
    String key = userId.getValue();
    jedis.incr(key);
    jedis.expire(key, 60);
    int numLogins = Integer.parseInt(jedis.get(key));
    return numLogins <= requestsAllowedPerMinute;
    }
}
