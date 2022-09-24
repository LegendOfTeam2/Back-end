package com.example.rhythme_backend.chat.repository;

import com.example.rhythme_backend.chat.domain.InvitedUsers;
import com.example.rhythme_backend.chat.domain.chat.ChatMessage;
import com.example.rhythme_backend.chat.domain.chat.ChatRoom;
import com.example.rhythme_backend.chat.dto.*;
import com.example.rhythme_backend.chat.service.RedisSubscriber;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    private final TokenProvider tokenProvider;
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final InvitedUsersRepository invitedUsersRepository;
    private final StringRedisTemplate stringRedisTemplate; // StringRedisTemplate 사용

    private final MemberRepository memberRepository;
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private static ValueOperations<String, String> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = stringRedisTemplate.opsForValue();
    }

    //내가 참여한 모든 채팅방 목록 조회
    @Transactional
    public ChatRoomListDto findAllRoom(HttpServletRequest request) {
        Member user = validateMember(request);
        List<ChatRoom> chatRooms = chatRoomJpaRepository.findByUsername(user.getNickname());
        List<ChatRoom> chatRooms1 = chatRoomJpaRepository.findByReceiver(user.getNickname());
        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
//            ChatMessage chatMessage = chatMessageJpaRepository.findTop1ByRoomIdOrderByCreatedAtDesc(chatRoom.getRoomId());
            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
//            if (chatMessage == null) {
//                chatRoomResponseDto.setLastMessage("콜라보 요청이 들어왔습니다.");
//            } else {
//                chatRoomResponseDto.setLastMessage(chatMessage.getMessage());
//            }
            LocalDateTime createdAt = LocalDateTime.now();
            String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("dd,MM,yyyy,HH,mm,ss", Locale.KOREA));
            chatRoomResponseDto.setRoomId(chatRoom.getRoomId());
            chatRoomResponseDto.setLastMessageTime(createdAtString);
            chatRoomResponseDto.setSender(user.getNickname());
            chatRoomResponseDto.setReceiver(chatRoom.getReceiver());
            chatRoomResponseDtoList.add(chatRoomResponseDto);
        }
        return new ChatRoomListDto(chatRoomResponseDtoList, true);
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
        if (topics.get(roomId) == null) {
            ChannelTopic topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.set(roomId, topic.toString());
            redisTemplate.expire(roomId, 48, TimeUnit.HOURS);
        } else {
            String topicToString = topics.get(roomId);
            ChannelTopic topic = new ChannelTopic(topicToString);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
        }
    }

    /*
     * 채팅방 생성 , 게시글 생성시 만들어진 postid를 받아와서 게시글 id로 사용한다.
     */
    @Transactional
    public ChatCreateResponseDto createChatRoom(UserinfoDto userinfoDto) {
        ChatRoom chatRoom = ChatRoom.create(userinfoDto);
//        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom); // redis 저장
//        redisTemplate.expire(CHAT_ROOMS, 48, TimeUnit.HOURS);
        chatRoom = chatRoomJpaRepository.save(chatRoom); // DB 저장
        Member receiver = memberRepository.findByNickname(userinfoDto.getReceiver()).orElseGet(Member::new);
        return  ChatCreateResponseDto.builder()
                .roomId(chatRoom.getRoomId())
                .sender(chatRoom.getUsername())
                .receiver(chatRoom.getReceiver())
                .receiverProfileUrl(receiver.getImageUrl())
                .build();
    }
    public static ChannelTopic getTopic(String roomId) {
        String topicToString = topics.get(roomId);
        return new ChannelTopic(topicToString);
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}