package com.example.rhythme_backend.dto.responseDto.post;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PostCreateResponseDto {
    private Long postId;
    private String email;
    private String position;
    private String title;
    private String content;
    private String imageUrl;
    private String mediaUrl;
    private List<String> tag;
}
