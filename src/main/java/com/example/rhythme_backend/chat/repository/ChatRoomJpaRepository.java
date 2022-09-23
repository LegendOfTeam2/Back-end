package com.example.rhythme_backend.chat.repository;

import com.example.rhythme_backend.chat.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByUsername(String username);
    ChatRoom findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
}
