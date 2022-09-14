package com.example.rhythme_backend.domain.chat;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.service.LikeService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    @Id
    private String roomId;
    @Column
    private String roomName;

    @OneToMany(mappedBy = "chatMessageId",fetch = FetchType.LAZY)
    private List<ChatMessage> chatMessage;


    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomId = UUID.randomUUID().toString();
        room.roomName = name;
        return room;
    }
}
