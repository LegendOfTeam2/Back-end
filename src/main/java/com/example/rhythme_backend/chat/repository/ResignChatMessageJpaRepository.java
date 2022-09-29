package com.example.rhythme_backend.chat.repository;


import com.example.rhythme_backend.chat.domain.ResignChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResignChatMessageJpaRepository extends JpaRepository<ResignChatMessage, Long> {
}
