package com.example.rhythme_backend.repository.like;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.like.SingerLike;
import com.example.rhythme_backend.domain.post.SingerPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SingerLikeRepository extends JpaRepository<SingerLike, Long> {
    Optional<SingerLike> findByMemberIdAndSingerPost(Member member, SingerPost singerPost);

    Long countAllBySingerPost(SingerPost singerPost);
    List<SingerLike> findBySingerPost(SingerPost singerPost);

    //Optional<Like> findByMemberAndPostId(Member member, Long postId);
    Long countAllBySingerPostId(Long singerPostId);
    Optional<SingerLike> deleteAllByMemberId(Member memberId);
    List<SingerLike> findAllByMemberIdOrderBySingerPost(Member memberId);
}
