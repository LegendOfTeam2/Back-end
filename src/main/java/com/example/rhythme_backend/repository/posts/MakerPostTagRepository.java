package com.example.rhythme_backend.repository.posts;


import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.MakerPostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MakerPostTagRepository extends JpaRepository<MakerPostTag,Long> {
    List<MakerPostTag> findAllById(Long id);

    List<MakerPostTag> deleteByMakerPostId(MakerPost makerPostId);

    List<MakerPostTag> findAllByMakerPostId(MakerPost makerPostId);

    Optional<MakerPostTag>deleteAllByMakerPostId(MakerPost makerPostId);
}
