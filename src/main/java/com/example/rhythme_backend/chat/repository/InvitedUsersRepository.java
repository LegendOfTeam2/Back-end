package com.example.rhythme_backend.chat.repository;

import com.example.rhythme_backend.chat.domain.InvitedUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitedUsersRepository extends JpaRepository<InvitedUsers, Long> {


    void deleteByUserAndRoomId(String userId, String roomId);
    boolean existsByUserAndRoomId(String user_id, String roomId);
    List<InvitedUsers> findAllByUser(String userId);

    List<InvitedUsers> findAllByRoomId(String postId);

//    List<InvitedUsers> findAllByUserIdAndReadCheck(Long userId, Boolean readCheck);
//    int countByPostId(Long postId);
//    boolean existsByPostId(Long id);
}
