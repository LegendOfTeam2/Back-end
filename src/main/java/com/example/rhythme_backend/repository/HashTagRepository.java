package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.HashTag;
import com.example.rhythme_backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag,Long> {

    Optional<HashTag> deleteAllByMemberId(Member memberId);

}
