package org.auth1.auth1.core.throttling;

import org.auth1.auth1.model.RedisConfiguration;
import org.auth1.auth1.model.RedisManager;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AuthenticationThrottler {

    private final Jedis jedis;
    private final int requestsAllowedPerMinute;

    public AuthenticationThrottler(RedisManager redisManager, RedisConfiguration redisConfiguration) {
        this.jedis = redisManager.getJedis();
        this.requestsAllowedPerMinute = redisConfiguration.getRequestsAllowedPerMinute();
    }

    public boolean loginAllowed(@Nullable String username, @Nullable String email, @Nullable String usernameOrEmail) {
        Optional<String> optionalLoginIdentifier = Stream.of(username, email, usernameOrEmail).filter(Objects::nonNull).findFirst();
        if(optionalLoginIdentifier.isPresent()) {
            String loginIdentifier = optionalLoginIdentifier.get();
            jedis.incr(loginIdentifier);
            jedis.expire(loginIdentifier, 60);
            int numLogins = Integer.parseInt(jedis.get(loginIdentifier));
            return numLogins <= requestsAllowedPerMinute;
        }
        return false;
    }
}
