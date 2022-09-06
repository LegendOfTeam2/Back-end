package com.example.rhythme_backend.dto.requestDto.member;

import lombok.Getter;

import java.util.List;

@Getter
public class SignupRequestDto {

    private String email;
    private String password;
    private String imgUrl;
    private String nickname;
    private List<String> hashtag;

}
