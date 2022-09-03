package com.example.rhythme_backend.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SingerLikeResponseDto {
    private Long singerPostId;
    private String nickname;
}
