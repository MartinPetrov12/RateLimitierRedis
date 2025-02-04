package com.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Instant;

public class RateLimiter {
    private final RedisCommands<String, String> redis;
    private final int limit;
    private final int window;

    public RateLimiter(String redisUrl, int limit, int window) {
        RedisClient redisClient = RedisClient.create(RedisURI.create(redisUrl));
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        this.redis = connection.sync();
        this.limit = limit;
        this.window = window;
    }

    public boolean isAllowed(String key) {
        long now = Instant.now().getEpochSecond(); // Current timestamp

        String redisKey = "rate_limiter:" + key;

        // Remove outdated entries
        redis.zremrangebyscore(redisKey, "0", String.valueOf(now - window));

        // Count the remaining requests
        long count = redis.zcount(redisKey, "-inf", "+inf");

        if (count >= limit) {
            return false; // Rate limit exceeded
        }

        // Add the new request timestamp
        redis.zadd(redisKey, now, String.valueOf(now));

        // Set expiration for key
        redis.expire(redisKey, window);

        return true;
    }
}
