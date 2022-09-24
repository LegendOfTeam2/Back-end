package com.example.rhythme_backend.chat.dto;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash
public class ChatCreateResponseDto {
    private String roomId;
    private String sender;
    private String receiver;
    private String receiverProfileUrl;
}
