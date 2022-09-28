package com.example.rhythme_backend.repository.chat;

import com.example.rhythme_backend.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
