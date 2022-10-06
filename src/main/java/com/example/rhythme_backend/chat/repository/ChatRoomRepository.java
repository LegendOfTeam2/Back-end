package com.example.rhythme_backend.chat.repository;

import com.example.rhythme_backend.chat.domain.chat.ChatMessage;
import com.example.rhythme_backend.chat.domain.chat.ChatRoom;
import com.example.rhythme_backend.chat.dto.ChatCreateResponseDto;
import com.example.rhythme_backend.chat.dto.ChatRoomListDto;
import com.example.rhythme_backend.chat.dto.ChatRoomResponseDto;
import com.example.rhythme_backend.chat.dto.UserinfoDto;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.Message;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {

    private final TokenProvider tokenProvider;
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner

    // 구독 처리 서비스
//    private final RedisSubscriber redisSubscriber;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final MemberRepository memberRepository;
    private static final String CHAT_ROOMS = "CHAT_ROOM";

    //내가 참여한 모든 채팅방 목록 조회
    @Transactional
    public ChatRoomListDto findAllRoom(HttpServletRequest request) {
        Member user = validateMember(request);
        List<ChatRoom> chatRooms = chatRoomJpaRepository.findByUsername(user.getNickname());
        List<ChatRoom> chatRooms1 = chatRoomJpaRepository.findByReceiver(user.getNickname());
        chatRooms.addAll(chatRooms1);
        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();

        if(chatRooms.size()==0){
            chatRoomResponseDtoList.add(ChatRoomResponseDto.builder()
                    .lastMessage("nullCheck")
                    .roomId("nullCheck")
                    .lastMessageTime("nullCheck")
                    .sender("nullCheck")
                    .receiver("nullCheck")
                    .senderProfileUrl("nullCheck")
                    .receiverProfileUrl("nullCheck")
                    .build());
            return new ChatRoomListDto(chatRoomResponseDtoList, true);
        }
        for (ChatRoom chatRoom : chatRooms) {
            Integer messageCheck = chatMessageJpaRepository.countChatMessageByRoomId(chatRoom.getRoomId());
            if(messageCheck==0){
                Member sender = memberRepository.findByNickname(chatRoom.getUsername()).orElseGet(Member::new);
                Member receiver = memberRepository.findByNickname(chatRoom.getReceiver()).orElseGet(Member::new);
                LocalDateTime createdAt = LocalDateTime.now();
                String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("dd,MM,yyyy,HH,mm,ss", Locale.KOREA));

                chatRoomResponseDtoList.add(ChatRoomResponseDto.builder()
                        .lastMessage("아직 메세지를 보내지 않았습니다.")
                        .roomId(chatRoom.getRoomId())
                        .lastMessageTime(createdAtString)
                        .sender(chatRoom.getUsername())
                        .receiver(chatRoom.getReceiver())
                        .senderProfileUrl(profileUrlCheck(sender))
                        .receiverProfileUrl(profileUrlCheck(receiver))
                        .build());
               continue;
            }else if(messageCheck>=1){
                ChatMessage lastMessage = chatMessageJpaRepository.findTop1ByRoomIdOrderByCreatedAtDesc(chatRoom.getRoomId());
                Member sender = memberRepository.findByNickname(chatRoom.getUsername()).orElseGet(Member::new);
                Member receiver = memberRepository.findByNickname(chatRoom.getReceiver()).orElseGet(Member::new);
                LocalDateTime createdAt = LocalDateTime.now();
                String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("dd,MM,yyyy,HH,mm,ss", Locale.KOREA));

                chatRoomResponseDtoList.add(ChatRoomResponseDto.builder()
                        .lastMessage(lastMessage.getMessage())
                        .roomId(chatRoom.getRoomId())
                        .lastMessageTime(createdAtString)
                        .sender(chatRoom.getUsername())
                        .receiver(chatRoom.getReceiver())
                        .senderProfileUrl(profileUrlCheck(sender))
                        .receiverProfileUrl(profileUrlCheck(receiver))
                        .build());
            }
        }
        return new ChatRoomListDto(chatRoomResponseDtoList, true);
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
    }

    /*
     * 채팅방 생성 , 게시글 생성시 만들어진 postid를 받아와서 게시글 id로 사용한다.
     */
    @Transactional
    public ResponseEntity<?> createChatRoom(UserinfoDto userinfoDto) {
        List<ChatRoom> chatRoomConfirmSender = chatRoomJpaRepository.findByUsername(userinfoDto.getSender());
        List<ChatRoom> chatRoomConfirmReceiver = chatRoomJpaRepository.findByReceiver(userinfoDto.getSender());
        chatRoomConfirmSender.addAll(chatRoomConfirmReceiver);
        ChatCreateResponseDto answer = new ChatCreateResponseDto();
        String noneAnswer = "이미 생성되어있는 방입니다.";
        ResponseEntity<?> result = new ResponseEntity<>(HttpStatus.OK);

        if(chatRoomConfirmSender.size()==0){
            ChatRoom chatRoom = ChatRoom.create(userinfoDto);
            chatRoom = chatRoomJpaRepository.save(chatRoom); // DB 저장
            Member receiver = memberRepository.findByNickname(userinfoDto.getReceiver()).orElseGet(Member::new);

            answer = ChatCreateResponseDto.builder()
                    .roomId(chatRoom.getRoomId())
                    .sender(chatRoom.getUsername())
                    .receiver(chatRoom.getReceiver())
                    .receiverProfileUrl(receiver.getImageUrl())
                    .build();
            result=  new ResponseEntity<>(Message.success(answer),HttpStatus.OK);
        }
        for(ChatRoom a : chatRoomConfirmSender){

            if( !chatRoomJpaRepository.existsByUsernameAndReceiver(userinfoDto.getSender(), userinfoDto.getReceiver())){
                if(!chatRoomJpaRepository.existsByUsernameAndReceiver(userinfoDto.getReceiver(),userinfoDto.getSender())) {

                    ChatRoom chatRoom1 = ChatRoom.create(userinfoDto);
                    chatRoom1 = chatRoomJpaRepository.save(chatRoom1); // DB 저장
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

    public String profileUrlCheck(Member member){
        String answer = "";
       if(member.getImageUrl()==null){
           return answer;
       }else{
           return member.getImageUrl();
       }
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}