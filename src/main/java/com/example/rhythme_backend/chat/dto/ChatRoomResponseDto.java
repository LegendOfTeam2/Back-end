package com.example.rhythme_backend.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private String lastMessage;
    private String postUrl;
    private String postTitle;
    private String lastMessageTime;
    private boolean isLetter;
    private String postTime;
    private Long postId;
}
