package com.example.rhythme_backend.dto.responseDto.post;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchMakerPostResponseDto {

    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private String imageUrl;
    private String mediaUrl;
    private Long makerlikeCnt;
    private Boolean collaborate;
}