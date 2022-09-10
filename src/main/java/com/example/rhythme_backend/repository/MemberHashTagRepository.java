package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.MemberHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberHashTagRepository extends JpaRepository<MemberHashTag,Long> {

    Optional<MemberHashTag> deleteAllByMemberId(Member memberId);

}
