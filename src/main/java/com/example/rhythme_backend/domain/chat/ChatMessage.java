package com.example.rhythme_backend.domain.chat;

import com.example.rhythme_backend.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MessageType type; // 메시지 타입
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    public static ChatMessage createChatMessage(ChatRoom chatRoom, String sender, String message,MessageType type) {
        ChatMessage chatMessage= ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .message(message)
                .type(type)
                .build();
        return chatMessage;
    }

    public void setSender(String sender){
        this.sender=sender;
    }

    public void setMessage(String message){
        this.message=message;
    }

    // 메시지 타입 : 입장, 퇴장, 채팅
    public enum MessageType {
        ENTER, QUIT, TALK
    }

}
