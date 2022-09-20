package com.example.rhythme_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@AutoConfiguration
@EnableRedisRepositories

public class RedisConfig {
        @Value("${redis.host}")
        private String redisHost;

        @Value("${redis.port}")
        private int redisPort;

        /***
         * Redis와 Connection 생성
         */
        @Bean
        public RedisConnectionFactory connectionFactory() {
            return new LettuceConnectionFactory(redisHost, redisPort);
        }




        /*
         * Redis 서버와 통신
         * StringRedisTemplate를 사용하여 Key, value를 모두 문자열로 저장
         */
        @Bean
        public StringRedisTemplate redisTemplate() {
            final StringRedisTemplate redisTemplate = new StringRedisTemplate();
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashValueSerializer(new StringRedisSerializer());
            redisTemplate.setConnectionFactory(connectionFactory());
            return redisTemplate;
        }

//    /**
//     * redis pub/sub 메시지를 처리하는 listener 설정
//     */
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

//    /**
//     * 어플리케이션에서 사용할 redisTemplate 설정
//     */
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
//        return redisTemplate;
//    }
}