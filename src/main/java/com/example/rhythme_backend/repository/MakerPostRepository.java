package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.post.MakerPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MakerPostRepository extends JpaRepository<MakerPost,Long> {
    @Override
    Optional<MakerPost> findById(Long id);
}
