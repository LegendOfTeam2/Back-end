package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.domain.Messages;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {


    @MessageMapping("/hello")
    @SendTo("/topic/roomId")
    public Messages boradCast(Messages messages){
        return messages;
    }
}