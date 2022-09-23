package com.example.rhythme_backend.chat.controller;


import com.example.rhythme_backend.chat.dto.ChatMessageDto;
import com.example.rhythme_backend.chat.dto.UserinfoDto;
import com.example.rhythme_backend.chat.repository.ChatMessageRepository;
import com.example.rhythme_backend.chat.repository.ChatRoomJpaRepository;
import com.example.rhythme_backend.chat.repository.ChatRoomRepository;
import com.example.rhythme_backend.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



@RequiredArgsConstructor
@Controller
@RequestMapping("/auth/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅 리스트 화면
//    @GetMapping("/room")
//    public String rooms(Model model) {
//        return "/chat/room";
//    }
//
    // 모든 채팅방 목록 반환
//    @GetMapping("/rooms")
//    @ResponseBody
//    public ChatRoomListDto roomList(HttpServletRequest request) {
//        return chatRoomRepository.findAllRoom(request);
//    }
//
    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<?> createRoom(@RequestBody UserinfoDto userinfoDto) {
        // 채팅방 레포에 생성.
        return new ResponseEntity<>(Message.success(chatRoomRepository.createChatRoom(userinfoDto)), HttpStatus.OK);
    }

    // 내 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public ResponseEntity<?> room( HttpServletRequest request) {
        return new ResponseEntity<>(Message.success(chatRoomRepository.findAllRoom(request)) ,HttpStatus.OK);
    }

    // 특정 채팅방 입장 채팅 방 메시지 내용을 준다.
    @PostMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<?> roomInfo(@PathVariable String roomId) {
        List<ChatMessageDto> chatMessageDtoList = chatMessageRepository.findAllMessage(roomId);
        return new ResponseEntity<>(Message.success(chatMessageDtoList),HttpStatus.OK);
    }



}