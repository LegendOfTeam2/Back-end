package com.example.rhythme_backend.chat.domain.chat;

import com.example.rhythme_backend.chat.dto.UserinfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatRoom implements Serializable {
//
//    private static final long serialVersionUID = 6494678977089006639L;
//
//    private String roomId;
//    private String name;
//
//    public static ChatRoom create(String name) {
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.roomId = UUID.randomUUID().toString();
//        chatRoom.name = name;
//        return chatRoom;

        private static final long serialVersionUID = 6494678977089006639L;

        @javax.persistence.Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long Id;
        @Column(nullable = false)
        private String roomId;
        @Column(nullable = false)
        private String username;
        @Column
        private String receiver;


        //채팅방 생성
        public static ChatRoom create( UserinfoDto userinfoDto) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.roomId = UUID.randomUUID().toString();
            chatRoom.username=userinfoDto.getSender();
            chatRoom.receiver=userinfoDto.getReceiver();
            return chatRoom;
        }

    }

