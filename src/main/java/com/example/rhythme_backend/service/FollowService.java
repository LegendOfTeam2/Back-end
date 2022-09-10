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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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
            System.out.println("팔로어 테이블 아이디: "+ follow.getId());
            System.out.println("찍힘당한 애: "+ follow.getFollowing().getId());
            System.out.println("찍은 애: "+follow.getMember().getId());
            Long followers = followRepository.countAllByFollowingId(memberId);
            System.out.println("찍힘당한 애 팔로워 수: "+followers);
            following.updateFollowers(followers);
            System.out.println("follower: 찍은 애"+follower.getId());
            System.out.println("following: 찍힘당한 애 "+following.getId());
            System.out.println("memberId: 찍힘당한 애 "+ memberId);
            memberRepository.save(following);
            return ResponseDto.success(true);
        } else {
            followRepository.deleteById(findFollowing.get().getId());
            Long followers = followRepository.countAllByFollowingId(memberId);
            follower.updateFollowers(followers);
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






