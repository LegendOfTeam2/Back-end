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
    private String receiverProfileUrl;

    public UserinfoDto(String sender , String receiver , String receiverProfileUrl) {
        this.sender = sender;
        this.receiver = receiver;
        this.receiverProfileUrl =receiverProfileUrl;
    }
}