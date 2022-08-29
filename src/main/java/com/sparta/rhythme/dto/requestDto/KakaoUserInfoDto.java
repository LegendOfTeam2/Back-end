package com.sparta.rhythme.dto.requestDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoUserInfoDto {

    private Long id;
    private String email;
    private String nickname;
}
