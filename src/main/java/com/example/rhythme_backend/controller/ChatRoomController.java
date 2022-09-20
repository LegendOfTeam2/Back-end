package com.example.rhythme_backend.controller;



import com.example.rhythme_backend.domain.chat.ChatRoom;
import com.example.rhythme_backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatService chatService;
    private final ResponseService responseService;
    private final UserServiceClient userServiceClient;


    @GetMapping("/rooms")
    public ListResult<ChatRoom> rooms() {
        return responseService.getListResult(chatService.findAllRoom());
    }


    @PostMapping("/room")
    public SingleResult<ChatRoom> createRoom(@RequestHeader("X-AUTH-TOKEN") String xAuthToken,@RequestBody UserIdDto store) {
        UserIdDto customer = userServiceClient.getUserId(xAuthToken);
        return responseService.getSingleResult(chatService.createChatRoom(customer,store));
    }


    @GetMapping("/room/{roomId}")
    public SingleResult<ChatRoom> roomInfo(@PathVariable String roomId) {
        return responseService.getSingleResult(chatService.findRoomById(roomId));
    }


    @GetMapping("/customer")
    public ListResult<ChatRoom> getRoomsByCustomer(@RequestHeader("X-AUTH-TOKEN") String xAuthToken){
        UserIdDto customer=userServiceClient.getUserId(xAuthToken);
        return responseService.getListResult(chatService.getCustomerEnterRooms(customer));
    }

    @GetMapping("/store")
    public ListResult<ChatRoom> getRoomsByStore(@RequestHeader("X-AUTH-TOKEN") String xAuthToken){
        UserIdDto store=userServiceClient.getUserId(xAuthToken);
        return responseService.getListResult(chatService.getCustomerEnterRooms(store));
    }
}