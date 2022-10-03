package com.example.rhythme_backend.chat.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class ChatRoomResponseDto {
    private String lastMessage;
    private String roomId;
    private String sender;
    private String senderProfileUrl;
    private String receiverProfileUrl;
    private String receiver;
    private String lastMessageTime;
}
