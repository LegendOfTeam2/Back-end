package com.example.rhythme_backend.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Setter
@Getter
@NoArgsConstructor
@RedisHash
public class ChatRoomResponseDto {
    private String lastMessage;
    private String roomId;
    private String sender;
    private String profileUrl;
    private String receiver;
    private String lastMessageTime;
    private Boolean duplicateCheck;
}
