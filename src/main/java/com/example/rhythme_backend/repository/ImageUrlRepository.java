package com.example.rhythme_backend.repository;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.media.ImageUrl;
import org.hibernate.engine.spi.ManagedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageUrlRepository extends JpaRepository<ImageUrl,Long> {

    Optional<ImageUrl> findByMember(Member member);
}
