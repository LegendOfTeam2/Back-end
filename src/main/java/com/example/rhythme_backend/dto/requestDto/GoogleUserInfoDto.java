package com.example.rhythme_backend.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GoogleUserInfoDto {

    private String googleId;
    private String email;
    private String name;
    private String nickname;

}
