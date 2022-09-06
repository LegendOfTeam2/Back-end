package com.example.rhythme_backend.dto.responseDto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PostPatchResponseDto {
    private Long postId;
    private String position;
    private String title;
    private String content;
    private String nickname;
    private String lyrics;
    private String imageUrl;
    private String mediaUrl;
    private List<String> tags;
}
