package com.example.redistest.chat.dto;

import com.example.redistest.chat.domain.chat.ChatMessage;

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
