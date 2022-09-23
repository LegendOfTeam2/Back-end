package com.example.rhythme_backend.chat.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatCreateResponseDto {
    private String roomId;
    private String sender;
    private String receiver;
    private String receiverProfileUrl;
}
