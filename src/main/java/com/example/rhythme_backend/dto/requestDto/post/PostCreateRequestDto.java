package com.example.rhythme_backend.dto.requestDto.post;

import lombok.Getter;

import java.util.List;

@Getter
public class PostCreateRequestDto {

    private String position;
    private String title;
    private String content;
    private String nickname;
    private String lyrics;
    private String imageUrl;
    private String mediaUrl;
    private List<String> tags;
    private Boolean collaborate;
}
