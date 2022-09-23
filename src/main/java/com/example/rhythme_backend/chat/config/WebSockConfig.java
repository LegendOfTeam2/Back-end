package com.example.redistest.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    /*
     * interceptors 설정
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration
                .interceptors(stompHandler);
    }

}