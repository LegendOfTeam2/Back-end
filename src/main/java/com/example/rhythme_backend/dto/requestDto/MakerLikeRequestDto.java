package com.example.rhythme_backend.dto.requestDto;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MakerLikeRequestDto {
    private Member member;
    private MakerPost makerPost;
}
