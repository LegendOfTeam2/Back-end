package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Follow;
import com.example.rhythme_backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByMemberAndFollowing(Member following, Member follower);

    //Long countByFollower(Member member);

    Long countByFollowing(Member member);

    Long countAllByFollowingId(Long memberId);
    Optional<Follow> deleteAllByMemberId(Member memberId);
    List<Follow> findAllByMemberOrderByFollowing(Member memberId);



}


