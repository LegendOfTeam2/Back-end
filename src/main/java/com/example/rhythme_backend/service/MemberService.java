package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.HashTag;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.Message;
import com.example.rhythme_backend.dto.TokenDto;
import com.example.rhythme_backend.dto.requestDto.member.*;
import com.example.rhythme_backend.dto.responseDto.ResignResponseDto;
import com.example.rhythme_backend.exception.CustomException;
import com.example.rhythme_backend.exception.ErrorCode;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.HashTagRepository;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.repository.RefreshTokenRepository;
import com.example.rhythme_backend.service.googleLogin.Constant;
import com.example.rhythme_backend.service.googleLogin.GoogleOauth;
import com.example.rhythme_backend.service.kakaoLogin.KakaoOauth;
import com.example.rhythme_backend.util.RefreshToken;
import com.example.rhythme_backend.util.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private final KakaoOauth kakaoOauth;
    private final HashTagRepository hashTagRepository;
    private final Validation validation;

    //============ 회원가입
    @Transactional
    public ResponseEntity<?> signupMember(SignupRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            return new ResponseEntity<>(Message.fail("DUPLICATED_EMAIL", "중복된 이메일입니다."), HttpStatus.BAD_REQUEST);
        }
        String defaultIntro = "리드미에 여러분을 소개해주세요!";
        Member member = Member.builder()
                .deleteCheck("N")
                .email(requestDto.getEmail())
                .imageUrl(requestDto.getImgUrl())
                .nickname(requestDto.getNickname())
                .followers(0L)
                .introduce(defaultIntro)
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);
        hashTagSave(requestDto.getHashtag(), member);
        return new ResponseEntity<>(Message.success("회원가입에 성공했습니다."), HttpStatus.OK);
    }

    //============ 이메일 중복 확인
    @Transactional
    public ResponseEntity<?> emailCheck(EmailCheckRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            return new ResponseEntity<>(Message.fail("DUPLICATED_EMAIL", "사용 불가능한 이메일입니다."), HttpStatus.OK);
        }
        return new ResponseEntity<>(Message.success("사용 가능한 이메일입니다."), HttpStatus.OK);
        }

    //============ 닉네임 중복 확인
    @Transactional
    public ResponseEntity<?> nicknameCheck(NicknameCheckRequestDto requestDto) {
        if (memberRepository.existsByNickname(requestDto.getNickname())) {
            return new ResponseEntity<>(Message.fail("DUPLICATED_NICKNAME","사용 불가능한 닉네임입니다."),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Message.success("사용 가능한 닉네임입니다."),HttpStatus.OK);
    }

    //============ 로그인 기능
    @Transactional
    public ResponseEntity<?> loginMember(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = validation.getPresentEmail(requestDto.getEmail());
        if (!memberRepository.existsByEmail(requestDto.getEmail())) {
            return new ResponseEntity<>(Message.fail("EMAIL_NOT_FOUND","존재하지 않는 이메일입니다."),HttpStatus.NOT_FOUND);
        }
        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return new ResponseEntity<>(Message.fail("PASSWORD_NOT_FOUND","비밀번호를 다시 입력해주세요."),HttpStatus.NOT_FOUND);
        }
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        validation.tokenToHeaders(tokenDto,response);
        return new ResponseEntity<>(Message.success("성공적으로 로그인 되었습니다."),HttpStatus.OK);
    }

    //============ 회원탈퇴 기능
    @Transactional
    public ResponseEntity<?> resignMember(ResignRequestDto requestDto, HttpServletRequest request) {
        validation.validateMemberToAccess(request);
        Member member = memberRepository.findByNickname(requestDto.getEmail()).orElseThrow(
                ()->new IllegalArgumentException("NICKNAME_NOT_FOUND"));
        RefreshToken deleteToken = validation.getDeleteToken(member);
        String deleteEmail = requestDto.getEmail();
        Member deleteMember = validation.getPresentEmail(deleteEmail);
        Long deleteMemberId = deleteMember.getId();
        Member resignMember = validation.getDeleteMember(deleteMemberId);
        refreshTokenRepository.delete(deleteToken);
        memberRepository.delete(resignMember);
        return new ResponseEntity<>(Message.success(
                ResignResponseDto.builder()
                        .email(deleteMember.getEmail())
                        .build()
        ),HttpStatus.OK);
    }

    //============ 로그아웃 기능
    public ResponseEntity<?> logoutMember(LogoutRequestDto requestDto, HttpServletRequest request) {
        validation.validateMemberToAccess(request);
        Member member = memberRepository.findByNickname(requestDto.getNickname()).orElseThrow(
                ()-> new IllegalArgumentException("NICKNAME_NOT_FOUND"));
        String logoutNickname = member.getNickname();
        Member logoutMember = validation.getPresentNickname(logoutNickname);
        Long logoutMemberId = logoutMember.getId();
        Member logout = validation.getDeleteMember(logoutMemberId);
        tokenProvider.deleteRefreshToken(logout);
        return new ResponseEntity<>(Message.success("로그아웃 되었습니다."),HttpStatus.OK);
    }

    //============ 카카오 로그인
    @Transactional
    public TokenDto kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = kakaoOauth.getAccessToken(code);
        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = kakaoOauth.getKakaoUserInfo(accessToken);
        // DB 에 중복된 KakaoId 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getKakaoid();
        Member kakaoUser = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
            // 회원가입
            String defaultIntro = "리드미에 여러분을 소개해주세요!";
            Random random = new Random();
            String kakaoDefaultName = "KAKAO" + random.nextInt(1000000000);
            String name = kakaoUserInfo.getName();
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            //카카오 이메일
            String email = kakaoUserInfo.getEmail();
            kakaoUser = Member.builder()
                    .deleteCheck("N")
                    .kakaoId(kakaoId)
                    .introduce(defaultIntro)
                    .followers(0L)
                    .nickname(kakaoDefaultName)
                    .email(email)
                    .name(name)
                    .password(encodedPassword)
                    .build();
            memberRepository.save(kakaoUser);
        }
        // 4. 강제 kakao로그인 처리
        UserDetails kakaouserDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(kakaouserDetails, null, kakaouserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Member member = validation.getPresentEmail(kakaoUser.getEmail());
        return tokenProvider.generateTokenDto(member);

    }

    //google
    @Transactional
    public void request(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        if (socialLoginType == Constant.SocialLoginType.GOOGLE) {//각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
            redirectURL = googleOauth.getOauthRedirectURL();
        } else {
            throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
        }
        response.sendRedirect(redirectURL);
    }


    @Transactional
    public TokenDto oAuthLogin(String code) throws IOException {
                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                GoogleOAuthTokenDto oAuthTokenDto = googleOauth.getAccessToken(accessTokenResponse);
                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                GoogleUserInfoDto googleUser=googleOauth.requestUserInfo(oAuthTokenDto);
                String googleId = googleUser.getGoogleId();
                Member googleLoginUser = memberRepository.findByGoogleId(googleId)
                        .orElse(null);
                if (googleLoginUser == null) {
                    // 회원가입
                    String defaultIntro = "리드미에 여러분을 소개해주세요!";
                    Random random = new Random();
                    String name = googleUser.getName();
                    String googleDefaultName = "google" + random.nextInt(1000000000);
                    String password = UUID.randomUUID().toString();
                    String encodedPassword = passwordEncoder.encode(password);
                    String email = googleUser.getEmail();
                    googleLoginUser = Member.builder()
                            .deleteCheck("N")
                            .introduce(defaultIntro)
                            .googleId(googleId)
                            .followers(0L)
                            .nickname(googleDefaultName)
                            .email(email)
                            .name(name)
                            .password(encodedPassword)
                            .build();
                    memberRepository.save(googleLoginUser);
                }
                // 4. 강제 구글로그인 처리
                UserDetails googleUserDetails = new UserDetailsImpl(googleLoginUser);
                Authentication authentication = new UsernamePasswordAuthenticationToken(googleUserDetails, null, googleUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                Member googleMember = validation.getPresentEmail(googleLoginUser.getEmail());
                return tokenProvider.generateTokenDto(googleMember);

    }

    //============ 리프레시토큰 발급
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        tokenProvider.validateToken(request.getHeader("Refresh-Token"));
        Member requestingMember = validation.validateMemberToRefresh(request);
        long accessTokenExpire = Long.parseLong(request.getHeader("Access-Token-Expire-Time"));
        long now = (new Date().getTime());

        if (now>accessTokenExpire){
            tokenProvider.deleteRefreshToken(requestingMember);
            throw new CustomException(ErrorCode.INVALID_TOKEN);}

        RefreshToken refreshTokenConfirm = refreshTokenRepository.findByMember(requestingMember).orElse(null);
        if (refreshTokenConfirm == null) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_IS_EXPIRED);
        }
        if (Objects.equals(refreshTokenConfirm.getValue(), request.getHeader("Refresh-Token"))) {
            TokenDto tokenDto = tokenProvider.generateAccessTokenDto(requestingMember);
            validation.accessTokenToHeaders(tokenDto, response);
            return new ResponseEntity<>(Message.success("ACCESS_TOKEN_REISSUE"), HttpStatus.OK);
        } else {
            tokenProvider.deleteRefreshToken(requestingMember);
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public void hashTagSave(List<String> hashtag,Member member){
        for(String tag : hashtag){
            hashTagRepository.save(
                    HashTag.builder()
                            .member(member)
                            .hashtag(tag)
                            .build());
        }
    }

//    리펙토링 할 때 사용하기 (삭제ㄴㄴ)
//    @Transactional(readOnly = true)
//    public Member getPresentEmail(String email) {
//        Optional<Member> optionalMember = memberRepository.findByEmail(email);
//        return optionalMember.orElseGet(()->new Member("notExist"));
//    }

}
