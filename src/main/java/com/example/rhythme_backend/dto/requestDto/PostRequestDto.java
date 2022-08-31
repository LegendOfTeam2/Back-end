package com.example.rhythme_backend.dto.requestDto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostRequestDto {

    private String email;
    private String position;
    private String title;
    private String content;
    private String imageUrl;
    private String mediaUrl;
    private List<String> tag;
}
