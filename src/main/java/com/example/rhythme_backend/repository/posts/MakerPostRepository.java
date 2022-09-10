package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MakerPostRepository extends JpaRepository<MakerPost,Long> {
    @Override
    Optional<MakerPost> findById(Long id);

    //@Query(value = "SELECT * FROM maker_post WHERE title LIKE %:searchText% or content LIKE %:searchText%", nativeQuery = true)
    //Page<MakerPost> findByTitleOrContent(String searchText, Pageable page);
    Page<MakerPost> findAll(Pageable page);
    List<MakerPost> findTop10ByOrderByCreatedAt();
    Optional<MakerPost> findTopByOrderByLikesDesc();
    List<MakerPost> findAllByOrderByLikesDesc();
    //Optional<MakerPost> deleteAllByMember(Member member);

}
