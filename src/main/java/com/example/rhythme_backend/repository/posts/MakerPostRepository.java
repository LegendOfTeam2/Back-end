package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MakerPostRepository extends JpaRepository<MakerPost,Long> {
    @Override
    Optional<MakerPost> findById(Long id);
    Page<MakerPost> findAll(Pageable pageable);
    @Query(value = "SELECT * FROM maker_post WHERE title LIKE %:searchText% or content LIKE %:searchText%", nativeQuery = true)
    Page<MakerPost> findByTitleOrContent(String searchText, Pageable page);
}
