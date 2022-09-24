package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.like.SingerLike;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.MakerPostTag;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.domain.post.SingerPostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SingerPostTagRepository extends JpaRepository<SingerPostTag,Long> {
    List<SingerPostTag> findAllById(Long id);
    List<SingerPostTag> deleteBySingerPostId(SingerPost singerPost);
    List<SingerPostTag> findAllBySingerPostId(SingerPost singerPost);
    Optional<SingerPostTag> deleteAllBySingerPostId(SingerPost singerPostId);
}
