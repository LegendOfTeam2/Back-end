package com.example.rhythme_backend.chat.repository;

import com.example.rhythme_backend.chat.domain.ResignChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResignChatRoomJpaRepository extends JpaRepository <ResignChatRoom, Long> {
    ResignChatRoom findByRoomId(String roomId);
}
