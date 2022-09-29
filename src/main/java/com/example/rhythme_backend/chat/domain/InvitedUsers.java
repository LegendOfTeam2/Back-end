package com.example.rhythme_backend.chat.domain;

import com.example.rhythme_backend.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InvitedUsers {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column
    private String roomId;
    @JoinColumn(name="user")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member user;
    @Column
    private Boolean readCheck;
    @Column
    private LocalDateTime readCheckTime;

    public InvitedUsers(String roomId, Member user) {
        this.roomId = roomId;
        this.user = user;
        this.readCheck =true;
    }


}