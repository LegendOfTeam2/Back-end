package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.domain.chat.ChatMessage;
import com.example.rhythme_backend.domain.chat.ChatRoom;
import com.example.rhythme_backend.service.ChatService;
import com.example.rhythme_backend.service.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@CrossOrigin
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatService chatService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @RequestBody String senderNickName) {
        // 로그인 회원 정보로 대화명 설정
        ChatRoom chatRoom=chatService.findRoomById(message.getChatRoom().getId());
        ChatMessage message1=ChatMessage.createChatMessage(chatRoom, senderNickName, message.getMessage(), message.getType());
        // 채팅방 입장시에는 대화명과 메시지를 자동으로 세팅한다.
        log.info("채팅 메시지");
        if (ChatMessage.MessageType.ENTER.equals(message1.getType())) {
            message1.setSender("[알림]");
            message1.setMessage(senderNickName + "님이 입장하셨습니다.");
        }else if(ChatMessage.MessageType.QUIT.equals(message1.getType())){
            message1.setSender("[알림]");
            message1.setMessage(senderNickName + "님이 퇴장하셨습니다.");
            chatService.deleteById(message1.getChatRoom());
        }
        chatRoom.addChatMessages(message1);
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        redisPublisher.publish(chatService.getTopic(message.getChatRoom().getId()), message1);
    }

}