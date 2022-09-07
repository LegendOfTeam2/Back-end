package com.example.rhythme_backend.dto.requestDto.like;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.SingerPost;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SingerLikeRequestDto {
    private Member member;
    private SingerPost singerPost;
}
