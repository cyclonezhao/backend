package com.backend.common.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class Config {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 配置redisTemplate
        RedisTemplate <String, Object> redisTemplate = new RedisTemplate <> ( );

        redisTemplate.setKeySerializer ( new StringRedisSerializer( ) );
        redisTemplate.setValueSerializer ( new JdkSerializationRedisSerializer( ) );
        redisTemplate.setHashKeySerializer ( new StringRedisSerializer ( ) );
        redisTemplate.setHashValueSerializer ( new StringRedisSerializer ( ) );

        redisTemplate.setConnectionFactory ( connectionFactory );
        return redisTemplate;
    }
}
