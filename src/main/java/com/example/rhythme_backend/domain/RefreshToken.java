package com.example.rhythme_backend.domain;

import com.example.rhythme_backend.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends Timestamped {

    @Id
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "members_id",nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "token_value", nullable = false)
    private String value;

    public void updateValue(String token){
        this.value = token;
    }

}
