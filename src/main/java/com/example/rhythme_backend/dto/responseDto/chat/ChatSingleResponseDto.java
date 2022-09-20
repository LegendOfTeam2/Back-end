package com.example.rhythme_backend.dto.responseDto.chat;


import com.example.rhythme_backend.util.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatSingleResponseDto<T> {
    private boolean success;
    private T data;

    public static <T> ChatSingleResponseDto<T> success(T data) {
        return new ChatSingleResponseDto<>(true, data);
    }
    public static <T> ChatSingleResponseDto<T> fail(T data) {
        return new ChatSingleResponseDto<>(false, null);
    }
}
