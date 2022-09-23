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
    private Long postId;
    @JoinColumn(name="USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member user;
    @Column
    private Boolean qrCheck;
    @Column
    private Boolean readCheck;
    @Column
    private LocalDateTime readCheckTime;

    public InvitedUsers(Long postId, Member user) {
        this.postId = postId;
        this.user = user;
        this.qrCheck = false;
        this.readCheck =true;
    }

    public void updateQrCheck() {
        this.qrCheck = true;
    }

}