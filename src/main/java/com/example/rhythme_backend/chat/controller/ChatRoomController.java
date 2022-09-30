package com.example.rhythme_backend.chat.controller;


import com.example.rhythme_backend.chat.dto.ChatMessageDto;
import com.example.rhythme_backend.chat.dto.ChatRoomListDto;
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
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<?> createRoom(@RequestBody UserinfoDto userinfoDto) {
        return chatRoomRepository.createChatRoom(userinfoDto);
    }
    // 내 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public ResponseEntity<?> room( HttpServletRequest request) {
        ChatRoomListDto answer = chatRoomRepository.findAllRoom(request);
        if(answer.getMessageDto().get(0).getLastMessage().equals("nullCheck")){
            return new ResponseEntity<>(Message.success("참여한 메세지 방이 없습니다."),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(Message.success(answer),HttpStatus.OK);
        }
    }

    // 특정 채팅방 입장 채팅 방 메시지 내용을 준다.
    @PostMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<?> roomInfo(@PathVariable String roomId) {
        List<ChatMessageDto> chatMessageDtoList = chatMessageRepository.findAllMessage(roomId);
        return new ResponseEntity<>(Message.success(chatMessageDtoList),HttpStatus.OK);
    }



}