package com.example.rhythme_backend.repository.posts;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.domain.post.SingerPostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SingerPostRepository extends JpaRepository<SingerPost,Long> {
    @Override
    Optional<SingerPost> findById(Long id);
    List<SingerPost> findTop10ByOrderByCreatedAt();
    List<SingerPost> findAllByMember(Member member);
    List<SingerPost> findTop10ByOrderByLikesDesc();
    List<SingerPost> findTopByOrderByLikesDesc();


}
