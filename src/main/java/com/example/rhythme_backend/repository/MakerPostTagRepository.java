package com.example.rhythme_backend.repository;


import com.example.rhythme_backend.domain.post.MakerPostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MakerPostTagRepository extends JpaRepository<MakerPostTag,Long> {
}
