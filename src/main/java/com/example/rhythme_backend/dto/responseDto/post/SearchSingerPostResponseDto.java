package com.example.rhythme_backend.dto.responseDto.post;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchSingerPostResponseDto {

    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private String imageUrl;
    private String mediaUrl;
    private Long singerlikeCnt;
    private Boolean collaborate;
}