package com.example.rhythme_backend.dto.responseDto.profile;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProfileUploadPostResponseDto{
    private Long postId;
    private String position;
    private String title;
    private String nickname;
    private String imageUrl;
    private String mediaUrl;
    private Boolean collaborate;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
