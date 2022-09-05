package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.post.SingerPostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SingerPostTagRepository extends JpaRepository<SingerPostTag,Long> {
    List<SingerPostTag> findAllById(Long id);
}
