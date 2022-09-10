package com.example.rhythme_backend.repository.like;


import com.example.rhythme_backend.domain.MemberHashTag;
import com.example.rhythme_backend.domain.like.MakerLike;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MakerLikeRepository extends JpaRepository<MakerLike, Long> {
    Optional<MakerLike> findByMemberIdAndMakerPost(Member member, MakerPost makerPost);
    List<MakerLike> findByMemberId(Member member);
    Long countAllByMakerPost(MakerPost makerPost);
    Long countAllByMakerPostId(Long makerPostId);
    List<MakerLike> findAllByMemberIdOrderByMakerPost(Member memberId);
}
