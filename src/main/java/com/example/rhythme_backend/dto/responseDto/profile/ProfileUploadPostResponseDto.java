package com.example.rhythme_backend.dto.responseDto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
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
