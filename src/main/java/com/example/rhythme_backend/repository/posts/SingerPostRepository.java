package com.example.rhythme_backend.repository.posts;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.SingerPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SingerPostRepository extends JpaRepository<SingerPost,Long> {
    @Override
    Optional<SingerPost> findById(Long id);
    List<SingerPost> findTop30ByOrderByCreatedAtDesc();
    List<SingerPost> findTop30ByOrderByLikesDesc();
    Page<SingerPost> findAllByMember(Member member, Pageable pageable);
    List<SingerPost> findTopByOrderByLikesDesc();
    List<SingerPost> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content);
    List<SingerPost> findAllByOrderByCreatedAt();
    Long countByMember(Member member);
}
