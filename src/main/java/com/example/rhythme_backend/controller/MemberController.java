package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.dto.TokenDto;
import com.example.rhythme_backend.dto.requestDto.EmailCheckRequestDto;
import com.example.rhythme_backend.dto.requestDto.LoginRequestDto;
import com.example.rhythme_backend.dto.requestDto.SignupRequestDto;
import com.example.rhythme_backend.service.MemberService;
import com.example.rhythme_backend.util.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/signup")
    public ResponseEntity<?> signupMember(@RequestBody SignupRequestDto requestDto) {
        return memberService.signupMember(requestDto);
    }

    @PostMapping("/member/emailcheck")
    public ResponseEntity<?> emailDubCheck(@RequestBody EmailCheckRequestDto requestDto) {
        return memberService.emailCheck(requestDto);
    }

    @PostMapping("/member/signin")
    public ResponseEntity<?> loginMember(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        return memberService.loginMember(requestDto,response);
    }

    @GetMapping("/api/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        TokenDto tokenDto= memberService.kakaoLogin(code);
        tokenDto.tokenToHeaders(response);
        return new ResponseEntity<>(Message.success("로그인에 성공하였습니다."), HttpStatus.OK);
    }

    @DeleteMapping("/auth/member")
    public ResponseEntity<?> resignMember(HttpServletRequest request) {
        return memberService.resignMember(request);
    }

}