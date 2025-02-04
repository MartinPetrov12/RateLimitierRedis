package com.controller;

import com.service.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {
    private RateLimiter rateLimiter;

    public RateLimiterController(@Value("${redis.url}") String redisUrl,
                                 @Value("${redis.limit}") Integer redisLimit,
                                 @Value("${redis.window}") Integer redisWindow) {
        this.rateLimiter = new RateLimiter(redisUrl, redisLimit, redisWindow);
    }

    @GetMapping("/sendRequest")
    public ResponseEntity<Integer> getZero() {
        if(rateLimiter.isAllowed("user-123"))
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        return new ResponseEntity<>(HttpStatusCode.valueOf(429));
    }
}
