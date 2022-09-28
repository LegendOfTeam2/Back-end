package com.example.rhythme_backend.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class ChatRoomResponseDto {
    private String lastMessage;
    private String roomId;
    private String sender;
    private String profileUrl;
    private String receiver;
    private String lastMessageTime;
}
