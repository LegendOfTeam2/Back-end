package com.example.rhythme_backend.domain.chat;

import com.example.rhythme_backend.domain.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ChatRoom")
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @Column
    private String roomId;
    private String roomName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ChatRoom_member",
            joinColumns = @JoinColumn(name = "chatRoomId"),
            inverseJoinColumns = @JoinColumn(name = "memberId"))
    private Set<Member> members = new HashSet<>();


    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomId = UUID.randomUUID().toString();
        room.roomName = name;
        return room;
    }
}