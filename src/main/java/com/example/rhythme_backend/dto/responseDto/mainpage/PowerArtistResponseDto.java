package com.example.rhythme_backend.dto.responseDto.mainpage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PowerArtistResponseDto {

    private String nickname;
    private Long follower;
    private String imageUrl;

}
