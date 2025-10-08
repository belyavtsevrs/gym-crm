package com.epam.gymsecurity.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class LoginAttemptsService {
    private final RedisTemplate<String,String> redisTemplate;
    private static final int MAX_FAILED = 3;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    public void loginFailed(String username){
        String key = "fail:" + username;
        Long attempts = redisTemplate.opsForValue().increment(key);
        if(attempts == 1){
            redisTemplate.expire(key,LOCK_DURATION);
        }
        if(attempts >= MAX_FAILED){
            redisTemplate.opsForValue().set("lock:" + username,"1",LOCK_DURATION);
        }
    }
    public boolean isLocked(String username) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("lock:" + username));
    }

    public void loginSucceeded(String username) {
        redisTemplate.delete("fail:" + username);
        redisTemplate.delete("lock:" + username);
    }
}
