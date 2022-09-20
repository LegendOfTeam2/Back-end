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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ChatRoom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private String id;

    private static final long serialVersionUID = 6494678977089006639L;

    private String name;

    private UserIdDto customer;

    private UserIdDto store;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public static ChatRoom create(String name,UserIdDto customer,UserIdDto store) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.customer=customer;
        chatRoom.store=store;
        return chatRoom;
    }

    public void addChatMessages(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }

}