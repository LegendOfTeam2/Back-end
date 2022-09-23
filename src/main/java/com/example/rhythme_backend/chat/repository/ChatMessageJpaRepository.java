package com.example.rhythme_backend.chat.repository;

import com.example.rhythme_backend.chat.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomId(String roomId);
    ChatMessage findTop1ByRoomIdOrderByCreatedAtDesc(String roomId);

    void deleteByRoomId(String RoomId);
}
