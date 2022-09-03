package com.example.rhythme_backend.domain;

import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.dto.requestDto.MakerLikeRequestDto;
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
@Table(name = "like_table")
public class MakerLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "makerpost_id")
    @JsonIgnore
    private MakerPost makerPost;


    @Builder
    public MakerLike(MakerLikeRequestDto requestDto) {
        this.member = requestDto.getMember();
        this.makerPost = requestDto.getMakerPost();

    }
}