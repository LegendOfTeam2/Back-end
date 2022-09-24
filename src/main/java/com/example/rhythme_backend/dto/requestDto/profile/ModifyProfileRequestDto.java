package com.example.rhythme_backend.dto.requestDto.profile;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyProfileRequestDto {
    private String nickname;
    private String imageUrl;
    private String introduce;
    private List<String> hashtag;
}
