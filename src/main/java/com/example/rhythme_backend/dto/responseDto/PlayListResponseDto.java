package com.example.rhythme_backend.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlayListResponseDto {

    private Long postId;
    private String title;
    private String mediaUrl;
    private String imageUrl;
    private Boolean collaborate;
    private String lyrics;
    private Long follower;
    private String nickname;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime createdAt;

}
