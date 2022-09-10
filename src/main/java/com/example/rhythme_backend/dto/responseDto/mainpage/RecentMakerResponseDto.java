package com.example.rhythme_backend.dto.responseDto.mainpage;

import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecentMakerResponseDto {

    private MediaUrl mediaUrl;
    private ImageUrl imageUrl;
    private String nickname;
    private String position;
    private String title;
    private Long likes;
    private Boolean collaborate;
    private String content;

}
