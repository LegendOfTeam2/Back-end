package com.example.rhythme_backend.dto.requestDto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoUserInfoDto {

    private Long kakaoid;
    private String email;
    private String name;
}
