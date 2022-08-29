package com.example.rhythme_backend.domain;

import com.example.rhythme_backend.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String name;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(unique = true)
    private Long kakaoId;


    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

}
