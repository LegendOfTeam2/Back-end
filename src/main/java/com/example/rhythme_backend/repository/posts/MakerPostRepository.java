package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface MakerPostRepository extends JpaRepository<MakerPost,Long> {
    @Override
    Optional<MakerPost> findById(Long id);

    List<MakerPost> findAllByMember(Member member);

    @Query(value = "SELECT * FROM maker_post WHERE title LIKE %:searchText% or content LIKE %:searchText%", nativeQuery = true)
    Page<MakerPost> findByTitleOrContent(String searchText, Pageable page);


    Page<MakerPost> findAll(Pageable page);
    List<MakerPost> findAllByOrderByCreatedAt();

    List<MakerPost> findTopByOrderByLikesDesc();

    List<MakerPost> findAllByOrderByLikesDesc();
}
