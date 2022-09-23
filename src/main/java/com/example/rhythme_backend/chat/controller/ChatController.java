package com.example.rhythme_backend.chat.controller;


import com.example.rhythme_backend.chat.domain.chat.ChatMessage;
import com.example.rhythme_backend.chat.dto.ChatMessageDto;
import com.example.rhythme_backend.chat.dto.UserinfoDto;
import com.example.rhythme_backend.chat.repository.ChatRoomRepository;
import com.example.rhythme_backend.chat.service.ChatService;
import com.example.rhythme_backend.chat.service.RedisPublisher;
import com.example.rhythme_backend.service.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;


// import 생략...

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

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
        @MessageMapping({"/chat/message"})
        public void message(ChatMessageDto message) throws JsonProcessingException {
            chatService.save(message);
        }

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