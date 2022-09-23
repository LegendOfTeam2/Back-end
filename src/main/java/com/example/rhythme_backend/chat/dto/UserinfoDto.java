package com.example.rhythme_backend.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserinfoDto {
    private String sender;
    private String receiver;

    public UserinfoDto(String sender , String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}