package com.example.rhythme_backend.chat.service;


//import com.example.rhythme_backend.chat.domain.chat.ChatMessage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.stereotype.Service;
//// import 생략...
//@RequiredArgsConstructor
//@Service
//public class RedisPublisher {
//    private final RedisTemplate<String, Object> redisTemplate;
//
//
//    // websocket 에서 받아온 메세지를 convertAndSend 를 통하여 Redis 의 messageListener 로 발행
//    public void publish(ChannelTopic topic, ChatMessage message) {
//        redisTemplate.convertAndSend(topic.getTopic(), message);
//    }
//}
