//package com.example.rhythme_backend.chat.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.IOException;
//// import 생략...
//
///**
// * 로컬 환경일경우 내장 레디스가 실행됩니다.
// */
//@Profile("local")
//@Configuration
//public class EmbeddedRedisConfig {
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//    private redis.embedded.RedisServer redisServer;
//
//    @PostConstruct
//    public void redisServer()  throws IOException {
//        redisServer = new redis.embedded.RedisServer(redisPort);
//        redisServer.start();
//    }
//
//    @PreDestroy
//    public void stopRedis() {
//        if (redisServer != null) {
//            redisServer.stop();
//        }
//    }
//}