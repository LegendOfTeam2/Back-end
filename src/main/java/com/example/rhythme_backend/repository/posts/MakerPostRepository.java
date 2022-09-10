package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MakerPostRepository extends JpaRepository<MakerPost,Long> {
    @Override
    Optional<MakerPost> findById(Long id);

//    @Query(value = "SELECT * FROM maker_post WHERE title LIKE %:searchText% or content LIKE %:searchText%"
//            ,nativeQuery = true)
    List<MakerPost> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title,String content);
    List<MakerPost> findAllByOrderByCreatedAt();

    List<MakerPost> findTopByOrderByLikesDesc();

    List<MakerPost> findAllByOrderByLikesDesc();
}
