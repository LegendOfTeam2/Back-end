package com.example.rhythme_backend.dto.responseDto.post;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchMemberResponseDto {

    private String nickname;
    private Long follower;
    private String imageUrl;
}