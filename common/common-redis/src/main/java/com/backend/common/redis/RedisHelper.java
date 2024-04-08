package com.backend.common.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisHelper {
    @Resource
    private RedisTemplate redisTemplate;

    private String getCacheKey(String cacheName, String key){
        return String.format ( "%s::%s", cacheName, key );
    }

    public void set(String cacheName, String key, Object value){
        this.set0 ( getCacheKey( cacheName, key ), value );
    }

    private void set0(String key, Object value) {
        redisTemplate.opsForValue ( ).set ( key, value );
    }

    public Boolean expire(String cacheName, String key, long timeout) {
        return redisTemplate.expire ( getCacheKey( cacheName, key ), timeout, TimeUnit.SECONDS );
    }
}
