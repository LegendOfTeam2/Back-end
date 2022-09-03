package com.example.rhythme_backend.repository.posts;


import com.example.rhythme_backend.domain.post.MakerPostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MakerPostTagRepository extends JpaRepository<MakerPostTag,Long> {
    List<MakerPostTag> findAllById(Long id);
}
