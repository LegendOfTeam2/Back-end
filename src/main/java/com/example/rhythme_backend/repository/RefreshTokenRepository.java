package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.util.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByMember(Member member);

}
