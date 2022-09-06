package com.example.rhythme_backend.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BestSongResponseDto {

    private Long id;
    private String mediaUrl;

}
