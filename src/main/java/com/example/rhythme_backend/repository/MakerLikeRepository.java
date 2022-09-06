package com.example.rhythme_backend.repository;


import com.example.rhythme_backend.domain.MakerLike;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MakerLikeRepository extends JpaRepository<MakerLike, Long> {


    Optional<MakerLike> findByMemberAndMakerPost(Member member, MakerPost makerPost);

    Long countAllByMakerPost(MakerPost makerPost);
    List<MakerLike> findByMakerPost(MakerPost makerPost);
    //Optional<Like> findByMemberAndPostId(Member member, Long postId);
}
