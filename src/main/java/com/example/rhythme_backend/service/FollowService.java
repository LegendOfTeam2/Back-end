package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.Follow;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.dto.requestDto.FollowRequestDto;
import com.example.rhythme_backend.exception.CustomException;
import com.example.rhythme_backend.exception.ErrorCode;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.FollowRepository;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;


    public ResponseDto<?> upDownFollow(Long memberId, HttpServletRequest request) {
        Member follower = validateMember(request);
        checkAccessToken(request, follower);
        Member following = isPresentMemberFollow(memberId);
        Optional<Follow> findFollowing = followRepository.findByMemberAndFollowing(follower, following);
        if(findFollowing.isEmpty()) {
            FollowRequestDto followRequestDto = new FollowRequestDto(follower, following);
            Follow follow = new Follow(followRequestDto);
            followRepository.save(follow);
            return ResponseDto.success(true);
        } else {
            followRepository.deleteById(findFollowing.get().getId());
            return ResponseDto.success(false);
        }
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

        public void checkAccessToken (HttpServletRequest request, Member member){
            if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7)))
                throw new CustomException(ErrorCode.TOKEN_IS_EXPIRED);
            if (null == member) throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
    public Member isPresentMemberFollow(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }
}






