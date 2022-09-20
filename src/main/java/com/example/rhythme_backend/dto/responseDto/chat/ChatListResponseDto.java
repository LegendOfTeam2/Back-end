package com.example.rhythme_backend.dto.responseDto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatListResponseDto<T> {
    private boolean success;
    private List<T> data;

    public static <T> ChatSingleResponseDto<T> success(T data) {
        return new ChatSingleResponseDto<>(true, data);
    }
    public static <T> ChatSingleResponseDto<T> fail(T data) {
        return new ChatSingleResponseDto<>(false, null);
    }
}
