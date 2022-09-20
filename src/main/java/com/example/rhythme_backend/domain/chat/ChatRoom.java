package com.example.rhythme_backend.domain.chat;



import lombok.Getter;
import lombok.Setter;


import java.util.List;

import java.util.UUID;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;

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

    private String customer;

    private String store;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

    public static ChatRoom create(String name,String senderNickName,String sendToNickName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.customer=senderNickName;
        chatRoom.store=sendToNickName;
        return chatRoom;
    }

    public void addChatMessages(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }


}