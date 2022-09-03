package com.example.rhythme_backend.dto.requestDto.member;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}
