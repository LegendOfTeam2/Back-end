package com.example.rhythme_backend.dto.requestDto.post;

import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
import lombok.Getter;

import java.util.List;

@Getter
public class PostPatchRequestDto {
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

