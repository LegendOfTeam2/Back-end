package com.example.rhythme_backend.chat.repository;

import com.example.rhythme_backend.chat.domain.InvitedUsers;
import com.example.rhythme_backend.chat.domain.chat.ChatMessage;
import com.example.rhythme_backend.chat.domain.chat.ChatRoom;
import com.example.rhythme_backend.chat.dto.ChatListMessageDto;
import com.example.rhythme_backend.chat.dto.ChatRoomResponseDto;
import com.example.rhythme_backend.chat.dto.UserDto;
import com.example.rhythme_backend.chat.dto.UserinfoDto;
import com.example.rhythme_backend.chat.service.RedisSubscriber;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.jwt.TokenProvider;
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
    public ChatListMessageDto findAllRoom(HttpServletRequest request) {
        Member user = validateMember(request);
        List<InvitedUsers> invitedUsers = invitedUsersRepository.findAllByUserId(user.getId());
        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();
        for (InvitedUsers invitedUser : invitedUsers) {
            if (invitedUser.getReadCheck()) {
                invitedUser.setReadCheck(false);
                invitedUser.setReadCheckTime(LocalDateTime.now());
            }
            Post post = postRepository.findById(invitedUser.getPostId()).orElseThrow(
                    () -> new PostApiException("해당 게시글을 찾을 수 없습니다."));
            ChatMessage chatMessage = chatMessageJpaRepository.findTop1ByRoomIdOrderByCreatedAtDesc(invitedUser.getPostId().toString());
            ChatRoomResponseDto chatRoomResponseDto = new ChatRoomResponseDto();
            if (chatMessage.getMessage().isEmpty()) {
                chatRoomResponseDto.setLastMessage("파일이 왔어요😲");
            } else {
                chatRoomResponseDto.setLastMessage(chatMessage.getMessage());
            }
            LocalDateTime createdAt = chatMessage.getCreatedAt();
            String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("dd,MM,yyyy,HH,mm,ss", Locale.KOREA));

            chatRoomResponseDto.setLastMessageTime(createdAtString);
            chatRoomResponseDto.setPostTime(post.getTime());
            chatRoomResponseDto.setPostTitle(post.getTitle());
            if (post.getPostUrls().isEmpty()) {
                chatRoomResponseDto.setPostUrl(null);
            } else {
                chatRoomResponseDto.setPostUrl(post.getPostUrls().get(0));
            }
            chatRoomResponseDto.setLetter(post.getIsLetter());
            chatRoomResponseDto.setPostId(post.getId());
            chatRoomResponseDtoList.add(chatRoomResponseDto);

        }
        return new ChatListMessageDto(chatRoomResponseDtoList, user.getIsOwner());
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
    public ChatRoom createChatRoom( UserinfoDto userDto) {
        ChatRoom chatRoom = ChatRoom.create(userDto);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom); // redis 저장
        redisTemplate.expire(CHAT_ROOMS, 48, TimeUnit.HOURS);
        chatRoom = chatRoomJpaRepository.save(chatRoom); // DB 저장
        return chatRoom;
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