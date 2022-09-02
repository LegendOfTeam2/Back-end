package com.example.rhythme_backend.repository;


import com.example.rhythme_backend.domain.post.SingerPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SingerPostRepository extends JpaRepository<SingerPost,Long> {
    @Override
    Optional<SingerPost> findById(Long id);
}