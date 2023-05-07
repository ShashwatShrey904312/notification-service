package com.shashwatshrey.notificationservice.smsrequest.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
    @Bean
    LettuceConnectionFactory jedisConnectionFactory() {
        return new LettuceConnectionFactory();
    }
    @Bean
    RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate= new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        //TODO - Add serializers


        return redisTemplate;
    }
}
