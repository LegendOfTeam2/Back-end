package com.example.rhythme_backend.chat.dto;


import com.example.rhythme_backend.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String nickName;
    private String profileUrl;
    private Long kakaoId;
    private String googleId;



    public UserDto(Member user) {
        this.username = user.getName();
        this.password = user.getPassword();
        this.nickName = user.getNickname();
        this.profileUrl = user.getImageUrl();
        this.kakaoId = user.getKakaoId();
        this.googleId = user.getGoogleId();
    }
}
