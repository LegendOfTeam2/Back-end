package com.example.rhythme_backend.dto.requestDto.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModifyProfileRequestDto {
    private String nickname;
    private String imageUrl;
    private String introduce;
    private List<String> hashtag;
}
