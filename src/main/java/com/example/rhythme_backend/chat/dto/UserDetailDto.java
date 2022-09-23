package com.example.rhythme_backend.chat.dto;

import com.example.rhythme_backend.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailDto {
    private String nickname;
    private String profileUrl;
    private Boolean response;
    private String message;
    private Boolean chatOwner;

    public UserDetailDto(Boolean response, String message, Member user, Boolean chatOwner) {
        this.response = response;
        this.message = message;
        this.nickname = user.getNickname();
        this.profileUrl = user.getImageUrl();
        this.chatOwner =chatOwner;
    }
}
