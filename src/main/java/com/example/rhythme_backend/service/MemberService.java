package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.HashTag;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.TokenDto;
import com.example.rhythme_backend.dto.requestDto.member.*;
import com.example.rhythme_backend.dto.responseDto.ResignResponseDto;
import com.example.rhythme_backend.exception.CustomException;
import com.example.rhythme_backend.exception.ErrorCode;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.*;
import com.example.rhythme_backend.repository.like.MakerLikeRepository;
import com.example.rhythme_backend.repository.like.SingerLikeRepository;
import com.example.rhythme_backend.repository.posts.MakerPostRepository;
import com.example.rhythme_backend.repository.posts.MakerPostTagRepository;
import com.example.rhythme_backend.repository.posts.SingerPostRepository;
import com.example.rhythme_backend.repository.posts.SingerPostTagRepository;
import com.example.rhythme_backend.service.googleLogin.Constant;
import com.example.rhythme_backend.service.googleLogin.GoogleOauth;
import com.example.rhythme_backend.service.kakaoLogin.KakaoOauth;
import com.example.rhythme_backend.util.Message;
import com.example.rhythme_backend.util.RefreshToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Jwts;
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
//    private final FollowRepository followRepository;
//    private final MakerLikeRepository makerLikeRepository;
//    private final SingerLikeRepository singerLikeRepository;
//    private final MakerPostTagRepository makerPostTagRepository;
//    private final SingerPostTagRepository singerPostTagRepository;
//    private final TagRepository tagRepository;
//    private final MakerPostRepository makerPostRepository;
//    private final SingerPostRepository singerPostRepository;

    
    @Transactional
    public ResponseEntity<?> signupMember(SignupRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            return new ResponseEntity<>(Message.fail("DUPLICATED_EMAIL","중복된 이메일입니다."),HttpStatus.BAD_REQUEST);
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
        hashTagSave(requestDto.getHashtag(),member);

        return new ResponseEntity<>(Message.success("회원가입에 성공했습니다."), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> emailCheck(EmailCheckRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            return new ResponseEntity<>(Message.fail("DUPLICATED_EMAIL","사용 불가능한 이메일입니다."), HttpStatus.OK);
        }
        return new ResponseEntity<>(Message.success("사용 가능한 이메일입니다."), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> nicknameCheck(NicknameCheckRequestDto requestDto) {
        if (memberRepository.existsByNickname(requestDto.getNickname())) {
            return new ResponseEntity<>(Message.fail("DUPLICATED_NICKNAME","사용 불가능한 닉네임입니다."),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Message.success("사용 가능한 닉네임입니다."),HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> loginMember(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = getPresentEmail(requestDto.getEmail());
        if (null == member) {
            return new ResponseEntity<>(Message.fail("EMAIL_NOT_FOUND","존재하지 않는 이메일입니다."),HttpStatus.NOT_FOUND);
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return new ResponseEntity<>(Message.fail("PASSWORD_NOT_FOUND","비밀번호를 다시 입력해주세요."),HttpStatus.NOT_FOUND);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto,response);
        return new ResponseEntity<>(Message.success("성공적으로 로그인 되었습니다."),HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> resignMember(ResignRequestDto requestDto, HttpServletRequest request) {

//        //기존 인증처리
//        if (null == request.getHeader("Refresh-Token")) {
//            return new ResponseEntity<>(Message.fail("MEMBER_NOT_FOUND","로그인이 필요합니다."),HttpStatus.UNAUTHORIZED);
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return new ResponseEntity<>(Message.fail("MEMBER_NOT_FOUND","로그인이 필요합니다."),HttpStatus.UNAUTHORIZED);
//        }
//
//        Member member = validateMember(request);
//        if (null == member) {
//            return new ResponseEntity<>(Message.fail("INVALID_TOKEN", "Token이 유효하지 않습니다."), HttpStatus.UNAUTHORIZED);
//        }

        String[] BearerSplit = request.getHeader("Authorization").split(" ");
        String accessToken = BearerSplit[1];
        if (!tokenProvider.validateToken(accessToken)) {
            return new ResponseEntity<>(Message.fail("INVALID_TOKEN", "토큰이 유효하지 않습니다."),HttpStatus.UNAUTHORIZED);
        }
        Member member = memberRepository.findByNickname(requestDto.getEmail()).orElse(null);
        if (member == null) {
            return new ResponseEntity<>(Message.fail("NICKNAME_NOT_FOUND", "존재하지 않는 닉네임입니다."),HttpStatus.BAD_REQUEST);
        }

        RefreshToken deleteToken = getDeleteToken(member);
        String deleteEmail = requestDto.getEmail();
        Member deleteMember = getPresentEmail(deleteEmail);
        Long deleteMemberId = deleteMember.getId();
        Member resignMember = getDeleteMember(deleteMemberId);
        if (null == resignMember) {
            return new ResponseEntity<>(Message.fail("MEMBER_NOT_FOUND", "해당 멤버가 없습니다."), HttpStatus.NOT_FOUND);
        }
        refreshTokenRepository.delete(deleteToken);
        memberRepository.delete(resignMember);

//        //실제 삭제
//        if (equals(hashTagRepository.findById(member.getId()))) {
//            hashTagRepository.deleteAllByMemberId(member);
//        }
//        if (equals(followRepository.findById(member.getId()))) {
//            followRepository.deleteAllByMemberId(member);
//        }
//        if (equals(makerLikeRepository.findById(member.getId()))) {
//            makerLikeRepository.deleteAllByMemberId(member);
//        }
//        if (equals(singerLikeRepository.findById(member.getId()))){
//            singerLikeRepository.deleteAllByMemberId(member);
//        }
//        Member makerPostDelete = memberRepository.findById(member.getId()).orElseGet(Member::new);
//        MakerPost makerPost = makerPostRepository.findById(makerPostDelete.getId()).orElseGet(MakerPost::new);
//        if (equals(makerPostRepository.findById(makerPostDelete.getId()))) {
//            makerPostTagRepository.deleteAllByMakerPostId(makerPost);
//            tagRepository.deleteAllByMakerPostTags(makerPost);
//        }
//        Member singerPostDelete = memberRepository.findById(member.getId()).orElseGet(Member::new);
//        SingerPost singerPost = singerPostRepository.findById(singerPostDelete.getId()).orElseGet(SingerPost::new);
//        if(equals(singerPostRepository.findById(singerPostDelete.getId()))) {
//            singerPostTagRepository.deleteAllBySingerPostId(singerPost);
//            tagRepository.deleteAllBySingerPostTags(singerPost);
//        }
//        if (equals(makerPostRepository.findById(member.getId()))) {
//            makerPostRepository.deleteAllByMember(member);
//        }
//        if (equals(singerPostRepository.findById(member.getId()))) {
//            singerPostRepository.deleteAllByMember(member);
//        }

//        String deleteEmail = requestDto.getEmail();
//        Member deleteMember = getPresentEmail(deleteEmail);
//        Long deleteMemberId = deleteMember.getId();
//        Member resignMember = getDeleteMember(deleteMemberId);

//        if (null == resignMember) {
//            return new ResponseEntity<>(Message.fail("MEMBER_NOT_FOUND", "해당 멤버가 없습니다."), HttpStatus.NOT_FOUND);
//        }
//        refreshTokenRepository.delete(deleteToken);
//        memberRepository.delete(resignMember);
        return new ResponseEntity<>(Message.success(
                ResignResponseDto.builder()
                        .email(deleteMember.getEmail())
                        .build()
        ),HttpStatus.OK);
    }

    public ResponseEntity<?> logoutMember(LogoutRequestDto requestDto, HttpServletRequest request) {

        String[] BearerSplit = request.getHeader("Authorization").split(" ");
        String accessToken = BearerSplit[1];
        if (!tokenProvider.validateToken(accessToken)) {
            return new ResponseEntity<>(Message.fail("INVALID_TOKEN", "토큰이 유효하지 않습니다."),HttpStatus.UNAUTHORIZED);
        }
        Member member = memberRepository.findByNickname(requestDto.getNickname()).orElse(null);
        if (member == null) {
            return new ResponseEntity<>(Message.fail("NICKNAME_NOT_FOUND", "존재하지 않는 닉네임입니다."),HttpStatus.BAD_REQUEST);
        }

        String logoutNickname = requestDto.getNickname();
        Member logoutMember = getPresentNickname(logoutNickname);
        Long logoutMemberId = logoutMember.getId();
        Member logout = getDeleteMember(logoutMemberId);
        tokenProvider.deleteRefreshToken(logout);
        return new ResponseEntity<>(Message.success("로그아웃 되었습니다."),HttpStatus.OK);
    }


    @Transactional
    public TokenDto kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = kakaoOauth.getAccessToken(code);
        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = kakaoOauth.getKakaoUserInfo(accessToken);
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getKakaoid();
        Member kakaoUser = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);

        if (kakaoUser == null) {
            // 회원가입
            String defaultIntro = "리드미에 여러분을 소개해주세요!";
            Random random = new Random();
            String kakaoDefaultName = "KAKAO" + random.nextInt(1000000000);
            String name = kakaoUserInfo.getName();
            String nickname = kakaoDefaultName;
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            //카카오 이메일
            String email = kakaoUserInfo.getEmail();
            kakaoUser = Member.builder()
                    .deleteCheck("N")
                    .kakaoId(kakaoId)
                    .introduce(defaultIntro)
                    .followers(0L)
                    .nickname(nickname)
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

        Member member = getPresentEmail(kakaoUser.getEmail());
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        return tokenDto;
    }

    //google
    @Transactional
    public void request(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType) {
            case GOOGLE: {
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                redirectURL = googleOauth.getOauthRedirectURL();
            }break;
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
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

                Member googleMember = getPresentEmail(googleLoginUser.getEmail());
                TokenDto googleTokenDto = tokenProvider.generateTokenDto(googleMember);
                return googleTokenDto;
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

    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        tokenProvider.validateToken(request.getHeader("Refresh-Token"));
        Member requestingMember = validateMember(request);
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
            accessTokenToHeaders(tokenDto, response);
            return new ResponseEntity<>(Message.success("ACCESS_TOKEN_REISSUE"), HttpStatus.OK);
        } else {
            tokenProvider.deleteRefreshToken(requestingMember);
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }


    @Transactional(readOnly = true)
    public Member getPresentEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    @Transactional(readOnly = true)
    public Member getPresentNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public Member getDeleteMember(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        return optionalMember.orElse(null);
    }

    public RefreshToken getDeleteToken(Member member) {
        Optional<RefreshToken> optionalMember = refreshTokenRepository.findByMember(member);
        return optionalMember.orElse(null);
    }


    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    public void accessTokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

}
