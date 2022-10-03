package com.example.rhythme_backend.domain;

import com.example.rhythme_backend.dto.requestDto.FollowRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member follower;   // (팔로우) 하는 유저

    @JoinColumn(name = "following_member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member following;    // (팔로우) 받는 유저


    public Follow(FollowRequestDto requestDto) {
        this.follower = requestDto.getMember();
        this.following = requestDto.getFollowing();
    }
}
