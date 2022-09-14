package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Tag;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {

    Optional<Tag> deleteAllByMakerPostTags(MakerPost makerPost);
    Optional<Tag> deleteAllBySingerPostTags(SingerPost singerPost);


}
