package com.example.rhythme_backend.domain.like;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.requestDto.like.SingerLikeRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SingerLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "singerpost_id")
    @JsonIgnore
    private SingerPost singerPost;


    @Builder
    public SingerLike(SingerLikeRequestDto requestDto) {
        this.memberId = requestDto.getMember();
        this.singerPost = requestDto.getSingerPost();
    }
}