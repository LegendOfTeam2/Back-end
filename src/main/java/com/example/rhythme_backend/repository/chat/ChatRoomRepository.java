package com.example.rhythme_backend.repository.chat;

import com.example.rhythme_backend.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

}
