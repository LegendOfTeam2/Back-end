package com.example.rhythme_backend.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleOAuthTokenDto {

    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;

}
