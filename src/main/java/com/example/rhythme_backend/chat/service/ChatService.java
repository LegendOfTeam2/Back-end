package com.example.rhythme_backend.chat.service;


import com.example.rhythme_backend.chat.domain.InvitedUsers;
import com.example.rhythme_backend.chat.domain.ResignChatMessage;
import com.example.rhythme_backend.chat.domain.ResignChatRoom;
import com.example.rhythme_backend.chat.domain.chat.ChatMessage;
import com.example.rhythme_backend.chat.domain.chat.ChatRoom;
import com.example.rhythme_backend.chat.dto.ChatMessageDto;
import com.example.rhythme_backend.chat.dto.UserinfoDto;
import com.example.rhythme_backend.chat.repository.*;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.service.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository userRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    private final InvitedUsersRepository invitedUsersRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ResignChatRoomJpaRepository resignChatRoomJpaRepository;
    private final ResignChatMessageJpaRepository resignChatMessageJpaRepository;


    @Transactional
    public void save(ChatMessageDto messageDto, Long userId) throws JsonProcessingException {
        // 토큰에서 유저 아이디 가져오기
        Member user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 사용자 입니다!")
        );
        LocalDateTime createdAt = LocalDateTime.now();
        String formatDate = createdAt.format(DateTimeFormatter.ofPattern("dd,MM,yyyy,HH,mm,ss", Locale.KOREA));
        messageDto.setSender(user.getNickname());
        messageDto.setProfileUrl(user.getImageUrl());
        messageDto.setCreatedAt(formatDate);
        messageDto.setUserId(user.getId());
        messageDto.setQuitOwner(false);

        //받아온 메세지의 타입이 ENTER 일때
        if (ChatMessage.MessageType.ENTER.equals(messageDto.getType())) {
            chatRoomRepository.enterChatRoom(messageDto.getRoomId());
            messageDto.setMessage(messageDto.getSender() + "님이 입장하셨습니다.");
            String roomId = messageDto.getRoomId();

            List<InvitedUsers> invitedUsersList = invitedUsersRepository.findAllByPostId(Long.parseLong(roomId));
            for (InvitedUsers invitedUsers : invitedUsersList) {
                if (invitedUsers.getUser().equals(user)) {
                    invitedUsers.setReadCheck(true);
                }
            }
            // 이미 그방에 초대되어 있다면 중복으로 저장을 하지 않게 한다.
            if (!invitedUsersRepository.existsByUserIdAndPostId(user.getId(), Long.parseLong(roomId))) {
                InvitedUsers invitedUsers = new InvitedUsers(Long.parseLong(roomId), user);
                invitedUsersRepository.save(invitedUsers);
            }
            //받아온 메세지 타입이 QUIT 일때
        } else if (ChatMessage.MessageType.QUIT.equals(messageDto.getType())) {
            messageDto.setMessage(messageDto.getSender() + "님이 나가셨습니다.");
            if (invitedUsersRepository.existsByUserIdAndPostId(user.getId(), Long.parseLong(messageDto.getRoomId()))) {
                invitedUsersRepository.deleteByUserIdAndPostId(user.getId(), Long.parseLong(messageDto.getRoomId()));
            }
            chatMessageJpaRepository.deleteByRoomId(messageDto.getRoomId());
        }
        chatMessageRepository.save(messageDto); // 캐시에 저장 했다.
        ChatMessage chatMessage = new ChatMessage(messageDto, createdAt);
        chatMessageJpaRepository.save(chatMessage); // DB 저장

        // Websocket 에 발행된 메시지를 redis 로 발행한다(publish)
        redisPublisher.publish(ChatRoomRepository.getTopic(messageDto.getRoomId()), chatMessage);
    }

    //redis에 저장되어있는 message 들 출력
    public List<ChatMessageDto> getMessages(String roomId) {
        return chatMessageRepository.findAllMessage(roomId);
    }



    //채팅방에 참여한 사용자 정보 조회
    public List<UserinfoDto> getUserinfo(UserDetailsImpl userDetails, String roomId) {
        userRepository.findById(userDetails.getMember().getId()).orElseThrow(
                () -> new IllegalArgumentException("테스트 에러 메세지")
        );
        List<InvitedUsers> invitedUsers = invitedUsersRepository.findAllByPostId(Long.parseLong(roomId));
        List<UserinfoDto> users = new ArrayList<>();
        for (InvitedUsers invitedUser : invitedUsers) {
            Member user = invitedUser.getUser();
            users.add(new UserinfoDto(user.getNickname(), user.getImageUrl(), user.getId()));
        }
        return users;
    }


}