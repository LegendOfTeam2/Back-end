package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.media.MediaUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaUrlRepository extends JpaRepository<MediaUrl,Long> {
    Optional<MediaUrl> findByMember(Member member);
}
