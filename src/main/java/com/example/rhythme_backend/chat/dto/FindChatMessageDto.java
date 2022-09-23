package com.example.rhythme_backend.chat.dto;

import com.example.rhythme_backend.chat.domain.chat.ChatMessage;

import java.time.LocalDateTime;

public interface FindChatMessageDto {

    ChatMessage.MessageType getType();
    String getRoomId();
    String getSender();
    String getMessage();
    String getProfileUrl();
    Long getEnterUserCnt();
    Long getUserId();
    LocalDateTime getCreatedAt();
    String getFileUrl();
    Boolean getQuitOwner();
}
