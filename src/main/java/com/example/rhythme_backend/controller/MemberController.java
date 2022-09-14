package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.dto.TokenDto;
import com.example.rhythme_backend.dto.requestDto.member.*;
import com.example.rhythme_backend.service.googleLogin.Constant;
import com.example.rhythme_backend.service.MemberService;
import com.example.rhythme_backend.util.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/signup")
    public ResponseEntity<?> signupMember(@RequestBody SignupRequestDto requestDto) {
        log.info("컨트롤러"+requestDto.getEmail());
        return memberService.signupMember(requestDto);
    }

    @PostMapping("/member/emailcheck")
    public ResponseEntity<?> emailDubCheck(@RequestBody EmailCheckRequestDto requestDto) {
        return memberService.emailCheck(requestDto);
    }

    @PostMapping("/member/nicknamecheck")
    public ResponseEntity<?> nicknameDubCheck(@RequestBody NicknameCheckRequestDto requestDto) {
        return memberService.nicknameCheck(requestDto);
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
    public ResponseEntity<?> resignMember(@RequestBody ResignRequestDto requestDto, HttpServletRequest request) {
        return memberService.resignMember(requestDto,request);
    }

    @PostMapping("/auth/member/signout")
    public ResponseEntity<?> signOutMember(@RequestBody LogoutRequestDto requestDto, HttpServletRequest request) {
        return memberService.logoutMember(requestDto,request);
    }


    @GetMapping("/api/google")
    public void socialLoginRedirect(String SocialLoginPath) throws IOException {
        Constant.SocialLoginType socialLoginType = Constant.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        memberService.request(socialLoginType);
    }

    @GetMapping("/api/google/callback")
    public ResponseEntity<?> googleLogin(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
        TokenDto socialOAuthResponseDto = memberService.oAuthLogin(code);
        socialOAuthResponseDto.tokenToHeaders(response);
        return new ResponseEntity<>(Message.success(socialOAuthResponseDto),HttpStatus.OK);
    }

    @PostMapping("/member/refreshtoken")
    public ResponseEntity<?> refreshTokenCheck(HttpServletRequest request, HttpServletResponse response){
        return memberService.refreshToken(request, response);
    }



}
