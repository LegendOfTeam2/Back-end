package com.sparta.rhythme.repository;

import com.sparta.rhythme.domain.Member;
import com.sparta.rhythme.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByMember(Member member);

}
