package com.example.rhythme_backend.dto.responseDto.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakerLikeResponseDto {
    private Long makerPostId;
    private String nickname;
}
