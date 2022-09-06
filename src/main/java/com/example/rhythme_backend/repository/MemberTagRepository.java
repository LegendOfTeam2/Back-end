package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.MemberTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberTagRepository extends JpaRepository<MemberTag,Long> {

    Optional<MemberTag> deleteAllByMemberId(Member memberId);

}
