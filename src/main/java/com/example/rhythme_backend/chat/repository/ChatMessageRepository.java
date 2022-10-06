package com.example.rhythme_backend.chat.repository;

import com.example.rhythme_backend.chat.domain.chat.ChatMessage;
import com.example.rhythme_backend.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {

    private static final String CHAT_MESSAGE = "CHAT_MESSAGE"; // 채팅룸에 메세지들을 저장
    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    private final ChatMessageJpaRepository chatMessageJpaRepository;


    //채팅 리스트 가져오기
    @Transactional
    public List<ChatMessageDto> findAllMessage(String roomId) {
        List<ChatMessageDto> chatMessageDtoList = new ArrayList<>();
            List<ChatMessage> chatMessages = chatMessageJpaRepository.findAllByRoomId(roomId);

            for (ChatMessage chatMessage : chatMessages) {
                LocalDateTime createdAt = chatMessage.getCreatedAt();
                String createdAtString = createdAt.format(DateTimeFormatter.ofPattern("dd,MM,yyyy,HH,mm,ss", Locale.KOREA));
                ChatMessageDto chatMessageDto = new ChatMessageDto(chatMessage,createdAtString);
                chatMessageDtoList.add(chatMessageDto);
            }
            //redis에 정보가 없으니, 다음부터 조회할때는 redis를 사용하기 위하여 넣어준다.
            return chatMessageDtoList;
        }

}
