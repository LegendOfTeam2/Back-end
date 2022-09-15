package com.example.rhythme_backend.domain.like;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.dto.requestDto.like.MakerLikeRequestDto;
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
public class MakerLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member memberId;

    // 게시글
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "makerpost_id")
    @JsonIgnore
    private MakerPost makerPost;


    @Builder
    public MakerLike(MakerLikeRequestDto requestDto) {
        this.memberId = requestDto.getMember();
        this.makerPost = requestDto.getMakerPost();

    }
}