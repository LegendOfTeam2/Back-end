package com.example.rhythme_backend.chat.repository;

import com.example.rhythme_backend.chat.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUsername(String username);
    ChatRoom findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
}
