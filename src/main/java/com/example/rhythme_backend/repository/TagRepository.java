package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.MemberTag;
import com.example.rhythme_backend.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {

    Optional<Tag>deleteAllByMemberId(Member memberId);

}
