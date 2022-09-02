package com.example.rhythme_backend.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialOAuthResponseDto {

    private String jwtToken;
    private int user_num;
    private String accessToken;
    private String tokenType;

}
