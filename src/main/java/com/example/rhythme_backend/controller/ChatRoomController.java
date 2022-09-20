package com.example.rhythme_backend.controller;



import com.example.rhythme_backend.domain.Message;
import com.example.rhythme_backend.domain.chat.ChatRoom;
import com.example.rhythme_backend.dto.responseDto.chat.ChatListResponseDto;
import com.example.rhythme_backend.dto.responseDto.chat.ChatSingleResponseDto;
import com.example.rhythme_backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.rhythme_backend.dto.responseDto.chat.ChatListResponseDto.success;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatService chatService;



    // 채팅방 전체 조회
    @GetMapping("/rooms")
    public ResponseEntity<?> rooms() {
        return new ResponseEntity<>(Message.success(chatService.findAllRoom()),HttpStatus.OK);
    }

    //채팅방 개설
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(String serndernickname, @RequestBody String sendToNickName) {
        return new ResponseEntity<>(Message.success(chatService.createChatRoom(serndernickname,sendToNickName)),HttpStatus.OK);
    }

    //채팅방 하나 정보 조회
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> roomInfo(@PathVariable String roomId) {
        return new ResponseEntity<>(Message.success(chatService.findRoomById(roomId)),HttpStatus.OK );
    }

    //만든 사람 별 채팅방 조회
    @GetMapping("/customer")
    public ResponseEntity<?> getRoomsByCustomer(@RequestBody String senderNickName){
        return new ResponseEntity<>(Message.success(chatService.getCustomerEnterRooms(senderNickName)),HttpStatus.OK );
    }

    // 초대 받은 사람 별 채팅방 조회
    @GetMapping("/store")
    public ResponseEntity<?> getRoomsByStore(@RequestBody String sendToNickName){
        return new ResponseEntity<>(Message.success(chatService.getStoreEnterRooms(sendToNickName)),HttpStatus.OK );
    }
}