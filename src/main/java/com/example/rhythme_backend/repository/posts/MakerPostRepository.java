package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Query;

=======
import java.util.List;
>>>>>>> e9e0947e38474b883a2d617f1dd6e93b3febdbc7
import java.util.Optional;

public interface MakerPostRepository extends JpaRepository<MakerPost,Long> {
    @Override
    Optional<MakerPost> findById(Long id);
<<<<<<< HEAD
    Page<MakerPost> findAll(Pageable pageable);
    @Query(value = "SELECT * FROM maker_post WHERE title LIKE %:searchText% or content LIKE %:searchText%", nativeQuery = true)
    Page<MakerPost> findByTitleOrContent(String searchText, Pageable page);
=======

    List<MakerPost> findAllByOrderByCreatedAt();

    List<MakerPost> findTopByOrderByLikesDesc();

    List<MakerPost> findAllByOrderByLikesDesc();

>>>>>>> e9e0947e38474b883a2d617f1dd6e93b3febdbc7
}
