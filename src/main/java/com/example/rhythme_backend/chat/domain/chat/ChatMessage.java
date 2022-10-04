package com.example.rhythme_backend.chat.domain.chat;

import com.example.rhythme_backend.chat.dto.ChatMessageDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅, 나가기
    public enum MessageType {
        ENTER, TALK, QUIT
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String roomId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;
    @Column(nullable = false)
    private String sender;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String profileUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;



    public ChatMessage(ChatMessageDto chatMessageDto, LocalDateTime createdAt) {
        this.type = chatMessageDto.getType();
        this.roomId = chatMessageDto.getRoomId();
        this.message = chatMessageDto.getMessage();
        this.sender = chatMessageDto.getSender();
        this.profileUrl = chatMessageDto.getProfileUrl();
        this.createdAt = createdAt;
    }
}
