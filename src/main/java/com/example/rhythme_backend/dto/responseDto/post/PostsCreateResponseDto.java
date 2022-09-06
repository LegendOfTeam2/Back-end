package com.example.rhythme_backend.dto.responseDto.post;

import com.example.rhythme_backend.util.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PostsCreateResponseDto extends Timestamped {
    private Long postId;
    private String nickname;
    private String position;
    private String title;
    private String content;
    private String lyrics;
    private String imageUrl;
    private String mediaUrl;
    private List<String> tags;
}
