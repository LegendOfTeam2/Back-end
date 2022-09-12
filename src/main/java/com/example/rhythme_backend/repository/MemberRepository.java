package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByKakaoId(Long kakaoId);
    Optional<Member> findByGoogleId(String googleId);
    Boolean existsByNickname(String nickname);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findById(Long id);
    List<Member> findTop30ByOrderByFollowersDesc();


}