package com.example.rhythme_backend.chat.controller;

import com.example.rhythme_backend.chat.domain.chat.ChatMessage;
import com.example.rhythme_backend.chat.dto.ChatMessageDto;
import com.example.rhythme_backend.chat.repository.ChatMessageJpaRepository;
import com.example.rhythme_backend.chat.repository.ChatMessageRepository;
import com.example.rhythme_backend.chat.repository.ChatRoomRepository;
import com.example.rhythme_backend.chat.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

    private final SimpMessageSendingOperations sendingOperations;

    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @MessageMapping("/chat/message")
    public void enter(ChatMessageDto message) {
        LocalDateTime now = LocalDateTime.now();
        ChatMessage chatMessage =new ChatMessage(message,now);
        sendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
        chatMessageJpaRepository.save(chatMessage);

    }

    //이전 채팅 기록 조회
    @GetMapping("/auth/chat/message/{roomId}")
    @ResponseBody
    public List<ChatMessageDto> getMessage(@PathVariable String roomId) {
        return chatService.getMessages(roomId);
    }


}