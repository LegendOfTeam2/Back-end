package com.example.rhythme_backend.dto.responseDto.post;

import com.example.rhythme_backend.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PostsCreateResponseDto {
    private Long postId;
    private String nickname;
    private String position;
    private String title;
    private String content;
    private String lyrics;
    private String imageUrl;
    private String mediaUrl;
    private List<String> tags;
    private Boolean collaborate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime modifiedAt;
}
