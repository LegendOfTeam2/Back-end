package com.example.rhythme_backend.dto.responseDto;

import com.example.rhythme_backend.domain.media.MediaUrl;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecentSingerResponseDto {

    private MediaUrl mediaUrl;

}
