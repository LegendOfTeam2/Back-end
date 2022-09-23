package com.example.redistest.chat.repository;

import com.example.redistest.chat.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByUsername(String username);
    ChatRoom findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
}
