package com.example.rhythme_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //MEMBER
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "M001", "해당 유저를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"M002","해당 이메일을 찾을 수 없습니다."),
    NOT_AUTHOR(HttpStatus.BAD_REQUEST.value(), "M003", "작성자가 아닙니다."),
    TOKEN_IS_EXPIRED(HttpStatus.BAD_REQUEST.value(), "M004", "만료된 액세스 토큰 입니다."),
    REFRESH_TOKEN_IS_EXPIRED(HttpStatus.BAD_REQUEST.value(), "M005", "만료된 리프레시 토큰 입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST.value(), "M006", "유효하지 않은 토큰 입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST.value(),"M007","이미 사용되고 있는 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST.value(),"M008","이미 사용되고 있는 닉네임입니다."),
    INVALID_MEMBER_INFO(HttpStatus.BAD_REQUEST.value(),"M009" ,"잘못된 사용자 정보입니다."),
    UNAUTHORIZED(HttpStatus.BAD_REQUEST.value(),"M010" ,"로그인이 필요합니다.");



    private final int httpStatus;
    private final String code;
    private final String message;


}
