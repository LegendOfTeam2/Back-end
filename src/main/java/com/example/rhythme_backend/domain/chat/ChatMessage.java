package com.example.rhythme_backend.domain.chat;

import com.example.rhythme_backend.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatMessageId")
    private Long chatMessageId;

    @Column
    private MessageType type;

    //채팅방 ID
    @ManyToOne
    private ChatRoom roomId;

    //보내는 사람
    @Column
    private String sender;

    //내용
    @Column
    private String message;

    public enum MessageType {
        ENTER, TALK
    }
}

