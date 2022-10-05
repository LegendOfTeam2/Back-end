package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MakerPostRepository extends JpaRepository<MakerPost,Long> {
    @Override
    Optional<MakerPost> findById(Long id);
    @Query
    List<MakerPost> findAllByMemberOrderByIdDesc(Member member);
    List<MakerPost> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content);
    List<MakerPost> findTopByOrderByLikesDesc();
    List<MakerPost> findTop30ByOrderByLikesDesc();
    List<MakerPost> findTop30ByOrderByCreatedAtDesc();
    Long countByMember(Member member);
}
