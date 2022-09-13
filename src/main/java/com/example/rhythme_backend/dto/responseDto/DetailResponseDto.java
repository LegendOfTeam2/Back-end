package com.example.rhythme_backend.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DetailResponseDto {
    private Long postId;
    private String position;
    private String title;
    private String content;
    private String nickname;
    private String lyrics;
    private String imageUrl;
    private String mediaUrl;
    private Boolean collaborate;
    private Long likes;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")

    private LocalDateTime modifiedAt;
    private List<String> tags;


}
