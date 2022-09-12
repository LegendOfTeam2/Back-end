package com.example.rhythme_backend.dto.responseDto.profile;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProfileResponseDto {
    private String nickname;
    private List<String> hashtag;
    private String introduce;
    private String imageUrl;
    private Long myPostConunt;
    private Long follower;
    private Long following;
}
