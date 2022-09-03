package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.post.SingerPostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingerPostTagRepository extends JpaRepository<SingerPostTag,Long> {
}
