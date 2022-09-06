package com.example.rhythme_backend.dto.responseDto.post;

import com.example.rhythme_backend.domain.Tag;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MakerPostGetResponseDto {
    private Long postId;
    private String email;
    private String position;
    private String title;
    private String content;
    private String imageUrl;
    private String mediaUrl;
    private List<Tag> tags;
    private Long makerlikeCnt;
}
