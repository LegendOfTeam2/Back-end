package com.example.rhythme_backend.dto.requestDto;

import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String email;
    private String password;
    private String imgUrl;
    private String position;
    private String nickname;

}
