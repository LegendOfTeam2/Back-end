package com.example.redistest.chat.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


// import 생략...


@RequiredArgsConstructor
@EnableCaching
@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }


    /**
     * redis pub/sub 메시지를 처리하는 listener 설정
    redisPublisher 에서 메세지가 발행(publish) 되면
    MessageListener 에서 처리합니다
    */
    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }


    /*
     * redisTemplate
     * Spring 에서 redis commend 들을 사용 할 수 있게 도와준다 (추상화 시켜준다)
     * RedisTemplate에는 serializer를 설정하여 데이터를 넘겨준다.
     */

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //일반적인 Key:value 경우 serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        //Hash 사용할 경우 serializer
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //redis connectionFactory 사용
        redisTemplate.setConnectionFactory(connectionFactory());
        return redisTemplate;
    }

    /*
     * String RedisTemplate 설정
     * 위에 선언한 RedisTemplate 보다 좀더 문자열에 특화된 Serialize 제공한다
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        final StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(connectionFactory());
        return stringRedisTemplate;
    }



}


















//@Configuration
//public class RedisConfig {
//
//    @Value("${redis.host}")
//    private String redisHost;
//
//    @Value("${redis.port}")
//    private int redisPort;
//
//    @Bean
//    public RedisConnectionFactory connectionFactory() {
//        return new LettuceConnectionFactory(redisHost, redisPort);
//    }
//
//    /**
//     * redis pub/sub 메시지를 처리하는 listener 설정
//     */
//    @Bean
//    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        return container;
//    }
//
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
//}