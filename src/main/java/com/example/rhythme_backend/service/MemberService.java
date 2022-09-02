package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.util.RefreshToken;
import com.example.rhythme_backend.dto.TokenDto;
import com.example.rhythme_backend.dto.requestDto.*;
import com.example.rhythme_backend.dto.responseDto.ResignResponseDto;
import com.example.rhythme_backend.exception.CustomException;
import com.example.rhythme_backend.exception.ErrorCode;
import com.example.rhythme_backend.service.googleLogin.Constant;
import com.example.rhythme_backend.service.googleLogin.GoogleOauth;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.repository.RefreshTokenRepository;
import com.example.rhythme_backend.service.kakaoLogin.KakaoOauth;
import com.example.rhythme_backend.util.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private final KakaoOauth kakaoOauth;


    @Transactional
    public ResponseEntity<?> signupMember(SignupRequestDto requestDto) {

        if (null != getPresentEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);

        return new ResponseEntity<>(Message.success("회원가입에 성공했습니다."), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> emailCheck(EmailCheckRequestDto requestDto) {

        if (null != getPresentEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }
        return new ResponseEntity<>(Message.success("사용 가능한 이메일입니다."), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> nicknameCheck(NicknameCheckRequestDto requestDto) {

        if (null != getPresentNickname(requestDto.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        }
        return new ResponseEntity<>(Message.success("사용 가능한 닉네임입니다."), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> loginMember(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = getPresentEmail(requestDto.getEmail());
        if (null == member) {
            return new ResponseEntity<>(Message.fail("EMAIL_DUPLICATED","이메일을 입력하세요."),HttpStatus.ALREADY_REPORTED);
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_MEMBER_INFO);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto,response);
        return new ResponseEntity<>(Message.success("성공적으로 로그인 되었습니다."),HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> resignMember(ResignRequestDto requestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return new ResponseEntity<>(Message.fail("MEMBER_NOT_FOUND","로그인이 필요합니다."),HttpStatus.UNAUTHORIZED);
        }

        if (null == request.getHeader("Authorization")) {
            return new ResponseEntity<>(Message.fail("MEMBER_NOT_FOUND","로그인이 필요합니다."),HttpStatus.UNAUTHORIZED);
        }

        Member member = validateMember(request);
        if (null == member) {
            return new ResponseEntity<>(Message.fail("INVALID_TOKEN", "Token이 유효하지 않습니다."), HttpStatus.UNAUTHORIZED);
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
        return new ResponseEntity<>(Message.success(
                ResignResponseDto.builder()
                        .email(deleteMember.getEmail())
                        .id(resignMember.getId())
                        .build()
        ),HttpStatus.OK);

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
            // username: kakao nickname
            String nickname = kakaoUserInfo.getNickname();

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            //카카오 이메일
            String email = kakaoUserInfo.getEmail();

            kakaoUser = Member.builder()
                    .kakaoId(kakaoId)
                    .email(email)
                    .nickname(nickname)
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

//    @Value("${myKaKaoRestAplKey}")
//    private String myKaKaoRestAplKey;
//
//    private String getAccessToken(String code) throws JsonProcessingException {
//
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP Body 생성
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", myKaKaoRestAplKey);
//        body.add("redirect_uri", "http://localhost:3000/kakao/callback");
//        body.add("code", code);
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
//                new HttpEntity<>(body, headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        return jsonNode.get("access_token").asText();
//    }

//    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
//
//        // HTTP Header 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        // HTTP 요청 보내기
//        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
//        RestTemplate rt = new RestTemplate();
//        ResponseEntity<String> response = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoUserInfoRequest,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        Long id = jsonNode.get("id").asLong();
//        String nickname = jsonNode.get("properties")
//                .get("nickname").asText();
//        String email = jsonNode.get("kakao_account")
//                .get("email").asText();
//
//
//        System.out.println("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
//        return KakaoUserInfoDto.builder()
//                .kakaoid(id)
//                .email(email)
//                .nickname(nickname)
//                .build();
//    }

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
                    // username/nickname
                    String name = googleUser.getName();
                    String nickname = googleUser.getNickname();

                    // password: random UUID
                    String password = UUID.randomUUID().toString();
                    String encodedPassword = passwordEncoder.encode(password);

                    //google 이메일
                    String email = googleUser.getEmail();

                    googleLoginUser = Member.builder()
                            .googleId(googleId)
                            .nickname(nickname)
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

}
