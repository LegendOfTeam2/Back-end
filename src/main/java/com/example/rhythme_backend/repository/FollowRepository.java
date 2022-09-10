package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Follow;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.MemberHashTag;
import com.example.rhythme_backend.domain.like.SingerLike;
import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(Member following, Member follower);


    List<Follow>findAllByFollowing(Member member);

    List<Follow>findAllByFollower(Member member);

    Long countByFollower(Member member);

    Long countByFollowing(Member member);

    Long countAllByFollowingId(String nickname);
    Optional<Follow> deleteAllByFollower(Member memberId);
    List<Follow> findAllByFollowerOrderByFollowing(Member memberId);



}


