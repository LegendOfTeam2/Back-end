package com.example.rhythme_backend.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayListResponseDto {

    private Long postId;
    private String title;
    private String mediaUrl;
    private String imageUrl;
    private Boolean collaborate;
    private String lyrics;
    private Long follower;
    private String nickname;

}
