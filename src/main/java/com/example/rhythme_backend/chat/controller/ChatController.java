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


// import 생략...

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
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
            chatRoomRepository.enterChatRoom(chatMessage.getRoomId());
        }

        sendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
        chatMessageJpaRepository.save(chatMessage);

    }

//    /**
//     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
//    기존 로직 ..
//     */
//    @MessageMapping("/chat/message")
//    public void message(ChatMessage message) {
//        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
//            chatRoomRepository.enterChatRoom(message.getRoomId());
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//        }
//        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
//        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
//    }


        /**
         * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
         */
//        @MessageMapping({"/chat/message"})
//        public void message(ChatMessageDto message) throws JsonProcessingException {
//            LocalDateTime now = LocalDateTime.now();
//            ChatMessage chatMessage =new ChatMessage(message,now);
//            if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
//            chatRoomRepository.enterChatRoom(message.getRoomId());
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//        }
//            // 초반 버전 message 로직
////            sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(),message);
////            redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()),chatMessage);
//            sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(),message);
//            chatService.save(message);
//        }

        //이전 채팅 기록 조회
        @GetMapping("/auth/chat/message/{roomId}")
        @ResponseBody
        public List<ChatMessageDto> getMessage(@PathVariable String roomId) {
            return chatService.getMessages(roomId);
        }


        //채팅방에 참여한 사용자 정보 조회
//        @GetMapping("/chat/message/userinfo/{roomId}")
//        @ResponseBody
//        public List<UserinfoDto> getUserInfo(
//                @PathVariable String roomId,
//                @AuthenticationPrincipal UserDetailsImpl userDetails) {
//            return chatService.getUserinfo(userDetails, roomId);
//        }

//        //유저 정보 상세 조회 (채팅방 안에서)
//        @GetMapping("/chat/details/{roomId}/{userId}")
//        @ResponseBody
//        public ResponseEntity<UserDetailDto> getUserDetails(@PathVariable String roomId, @PathVariable Long userId) {
//            return chatService.getUserDetails(roomId,userId);
//        }



}