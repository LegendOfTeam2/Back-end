package com.example.rhythme_backend.dto.requestDto;

import com.example.rhythme_backend.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowRequestDto {
    private Member member;
    private Member following;

    public FollowRequestDto(Member member, Member following) {
        this.member = member;
        this.following = following;
    }
}
