package com.example.rhythme_backend.repository.chat;

import com.example.rhythme_backend.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {

    List<ChatRoom> findChatRoomsByCustomer(String senderNickName);
    List<ChatRoom> findChatRoomsByStore(String sendToNickName);
}