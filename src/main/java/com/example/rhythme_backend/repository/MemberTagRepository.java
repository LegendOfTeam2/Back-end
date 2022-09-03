package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.MemberTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTagRepository extends JpaRepository<MemberTag,Long> {
}
