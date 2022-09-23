package com.example.redistest.chat.controller;

import com.example.redistest.chat.domain.chat.ChatMessage;
import com.example.redistest.chat.service.RedisPublisher;
import com.example.redistest.chat.repository.ChatRoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


// import 생략...

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }









        /**
         * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
         */
        @MessageMapping({"/chat/message"})
        public void message(ChatMessageDto message, @Header("PK") Long pk) throws JsonProcessingException {
            chatService.save(message, pk);
        }

        //이전 채팅 기록 조회
        @GetMapping("/chat/message/{roomId}")
        @ResponseBody
        public List<ChatMessageDto> getMessage(@PathVariable String roomId) {
            return chatService.getMessages(roomId);
        }

        //채팅방에 파일 넣을때 url 빼오기
        @PostMapping("/chat/message/file")
        @ResponseBody
        public String getMessage(@RequestPart(value = "file") MultipartFile file,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
            return chatService.getFileUrl(file, userDetails);
        }

        //채팅방에 참여한 사용자 정보 조회
        @GetMapping("/chat/message/userinfo/{roomId}")
        @ResponseBody
        public List<UserinfoDto> getUserInfo(
                @PathVariable String roomId,
                @AuthenticationPrincipal UserDetailsImpl userDetails) {
            return chatService.getUserinfo(userDetails, roomId);
        }
        //햄버거 버튼 눌렀을때, fileUrl들을 보낸다
        @GetMapping("/chat/message/files/{roomId}")
        @ResponseBody
        public List<FilesDto> getFiles(
                @PathVariable String roomId,
                @AuthenticationPrincipal UserDetailsImpl userDetails) {
            return chatService.getFiles(userDetails, roomId);
        }

        //유저 정보 상세 조회 (채팅방 안에서)
        @GetMapping("/chat/details/{roomId}/{userId}")
        @ResponseBody
        public ResponseEntity<UserDetailDto> getUserDetails(@PathVariable String roomId, @PathVariable Long userId) {
            return chatService.getUserDetails(roomId,userId);
        }




}