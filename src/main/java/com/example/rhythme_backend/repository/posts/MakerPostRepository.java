package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MakerPostRepository extends JpaRepository<MakerPost,Long> {
    @Override
    Optional<MakerPost> findById(Long id);


    List<MakerPost> findAllByMember(Member member, Pageable pageable);
    List<MakerPost> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content, Pageable pageable);
    List<MakerPost> findTopByOrderByLikesDesc();
    List<MakerPost> findAllByOrderByLikesDesc();
    List<MakerPost> findAllByOrderByCreatedAtDesc();
    Long countByMember(Member member);

}
