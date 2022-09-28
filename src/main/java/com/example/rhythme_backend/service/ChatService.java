package com.example.rhythme_backend.service;


import com.example.rhythme_backend.domain.Message;
import com.example.rhythme_backend.domain.chat.ChatMessage;
import com.example.rhythme_backend.domain.chat.ChatRoom;
import com.example.rhythme_backend.repository.chat.ChatMessageRepository;
import com.example.rhythme_backend.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private Map<String, ChatRoom> chatRooms;
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @PostConstruct // 최초 한번만 Bean 주입하는 어노테이션 반복 주입할 필요없다
    // 왜? 소켓으로 연결되어 있는데 계속 빈을 주입할 필요는 없다.
    //의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기
    public List<ChatRoom> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }

    //채팅방 하나 불러오기
    public ChatRoom findById(String roomId) {
        return chatRooms.get(roomId);
    }

    //채팅방 생성

    @Transactional
    public ResponseEntity<?> createChatRoom(UserinfoDto userinfoDto) {
        List<ChatRoom> chatRoomConfirmSender = chatRoomRepository.findByUsername(userinfoDto.getSender());
        List<ChatRoom> chatRoomConfirmReceiver = chatRoomRepository.findByReceiver(userinfoDto.getSender());
        chatRoomConfirmSender.addAll(chatRoomConfirmReceiver);
        ChatCreateResponseDto answer = new ChatCreateResponseDto();
        String noneAnswer = "이미 생성되어있는 방입니다.";
        ResponseEntity<?> result = new ResponseEntity<>(HttpStatus.OK);

        if(chatRoomConfirmSender.size()==0){
            ChatRoom chatRoom = ChatRoom.create(userinfoDto);
            opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom); // redis 저장
            redisTemplate.expire(CHAT_ROOMS, 48, TimeUnit.HOURS);
            chatRoom = chatRoomRepository.save(chatRoom); // DB 저장
            Member receiver = memberRepository.findByNickname(userinfoDto.getReceiver()).orElseGet(Member::new);
            answer = ChatCreateResponseDto.builder()
                    .roomId(chatRoom.getRoomId())
                    .sender(chatRoom.getUsername())
                    .receiver(chatRoom.getReceiver())
                    .receiverProfileUrl(receiver.getImageUrl())
                    .build();
            result=  new ResponseEntity<>(Message.success(answer), HttpStatus.OK);
        }
        for(ChatRoom a : chatRoomConfirmSender){
//            if(!a.getReceiver().equals(userinfoDto.getReceiver())&&!a.getReceiver().equals(userinfoDto.getSender())){
            if( !chatRoomRepository.existsByUsernameAndReceiver(userinfoDto.getSender(), userinfoDto.getReceiver())){
                if(!chatRoomRepository.existsByUsernameAndReceiver(userinfoDto.getReceiver(),userinfoDto.getSender())) {
                    ChatRoom chatRoom1 = ChatRoom.create(userinfoDto);
                    opsHashChatRoom.put(CHAT_ROOMS, chatRoom1.getRoomId(), chatRoom1); // redis 저장
                    redisTemplate.expire(CHAT_ROOMS, 48, TimeUnit.HOURS);
                    chatRoom1 = chatRoomRepository.save(chatRoom1); // DB 저장
                    Member receiver = memberRepository.findByNickname(userinfoDto.getReceiver()).orElseGet(Member::new);

                    answer = ChatCreateResponseDto.builder()
                            .roomId(chatRoom1.getRoomId())
                            .sender(chatRoom1.getUsername())
                            .receiver(chatRoom1.getReceiver())
                            .receiverProfileUrl(receiver.getImageUrl())
                            .build();

                    result=  new ResponseEntity<>(Message.success(answer),HttpStatus.OK);
                    break;
                }
                result= new ResponseEntity<>(Message.fail("duplicated",noneAnswer), HttpStatus.OK);
            }
            result= new ResponseEntity<>(Message.fail("duplicated",noneAnswer), HttpStatus.OK);
        }
        return  result;
    }
    
    public ChatRoom createRoom(String name) {


        
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}
