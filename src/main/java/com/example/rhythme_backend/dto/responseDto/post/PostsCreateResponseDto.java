package com.example.rhythme_backend.dto.responseDto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PostsCreateResponseDto {
    private Long postId;
    private String email;
    private String position;
    private String title;
    private String content;
    private String imageUrl;
    private String mediaUrl;
    private List<String> tags;
}
