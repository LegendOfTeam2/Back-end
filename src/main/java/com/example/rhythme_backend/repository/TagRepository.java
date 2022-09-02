package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
}
