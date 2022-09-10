package com.example.rhythme_backend.dto.responseDto.profile;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ModifyProfileResponseDto {
    private String nickname;
    private String imageUrl;
    private String introduction;
    private List<String> hashtag;
}
