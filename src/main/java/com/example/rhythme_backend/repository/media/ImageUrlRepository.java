package com.example.rhythme_backend.repository.media;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import org.hibernate.engine.spi.ManagedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageUrlRepository extends JpaRepository<ImageUrl,Long> {

}
