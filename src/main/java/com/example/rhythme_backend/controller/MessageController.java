package com.example.rhythme_backend.controller;


import com.example.rhythme_backend.domain.chat.ChatMessage;
import com.example.rhythme_backend.repository.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat/message")
    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }

        sendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
        chatMessageRepository.save(message);

    }


}