package com.example.rhythme_backend.chat.controller;


import com.example.rhythme_backend.chat.domain.chat.ChatRoom;
import com.example.rhythme_backend.chat.dto.ChatListMessageDto;
import com.example.rhythme_backend.chat.dto.UserinfoDto;
import com.example.rhythme_backend.chat.repository.ChatRoomJpaRepository;
import com.example.rhythme_backend.chat.repository.ChatRoomRepository;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;

//    // 채팅 리스트 화면
//    @GetMapping("/room")
//    public String rooms(Model model) {
//        return "/chat/room";
//    }
//
//    // 모든 채팅방 목록 반환
//    @GetMapping("/rooms")
//    @ResponseBody
//    public ChatListMessageDto room(HttpServletRequest request) {
//        return chatRoomRepository.findAllRoom(request);
//    }
//
//    // 채팅방 생성
//    @PostMapping("/room")
//    @ResponseBody
//    public ChatRoom createRoom(@RequestParam UserinfoDto userinfoDto) {
//        return chatRoomRepository.createChatRoom(userinfoDto);
//    }
//    // 채팅방 입장 화면
//    @GetMapping("/room/enter/{roomId}")
//    public String roomDetail(Model model, @PathVariable String roomId) {
//        model.addAttribute("roomId", roomId);
//        return "/chat/roomdetail";
//    }
//    // 특정 채팅방 조회
//    @GetMapping("/room/{roomId}")
//    @ResponseBody
//    public ChatRoom roomInfo(@PathVariable String roomId) {
//        return chatRoomJpaRepository.findByRoomId(roomId);
//    }
//
//


//    private final ChatRoomRepository chatRoomRepository;

    // 내 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public ChatListMessageDto room(@AuthenticationPrincipal UserDetailsImpl userDetails , HttpServletRequest request) {
        Member user = userDetails.getMember();
        return chatRoomRepository.findAllRoom(request);
    }

    // 특정 채팅방 입장
    @PostMapping("/room/{postId}")
    @ResponseBody
    public String roomInfo(@PathVariable Long postId) {
        return String.valueOf(postId);
    }





}