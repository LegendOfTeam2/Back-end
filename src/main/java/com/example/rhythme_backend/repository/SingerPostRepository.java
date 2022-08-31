package com.example.rhythme_backend.repository;


import com.example.rhythme_backend.domain.post.SingerPost;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SingerPostRepository extends JpaRepository<SingerPost,Long> {
}
