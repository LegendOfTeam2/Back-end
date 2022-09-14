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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transaction;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {
    private final FollowRepository followRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> upDownFollow(String nickname, HttpServletRequest request) {
        Member follower = validateMember(request);
        checkAccessToken(request, follower);
        Member following = isPresentMemberFollow(nickname);
        Optional<Follow> findFollowing = followRepository.findByFollowerAndFollowing(follower, following);
        
        if(findFollowing.isEmpty()) {
            FollowRequestDto followRequestDto = new FollowRequestDto(follower, following);
            Follow follow = new Follow(followRequestDto);
            followRepository.save(follow);
            Long followers = followRepository.countAllByFollowingId(following.getId());
            following.updateFollowers(followers);
            return ResponseDto.success(true);
        } else {
            followRepository.deleteById(findFollowing.get().getId());
            Long followers = followRepository.countAllByFollowingId(following.getId());
            follower.updateFollowers(followers);
            return ResponseDto.success(false);
        }
    }
        //  팔로우 // 팔로워 리스트 가져오는 서비스 로직// 챌린지로 추후 이야기 하기로 함.
//    @Transactional(readOnly = true)
//    public ResponseEntity<?> getMemberByAllFollower(String nickname, HttpServletRequest request) {
//        validateMember(request);
//        Member member = isPresentMemberFollow(nickname);
//        List<Follow> followList = followRepository.findAllByFollowing(member);
//        List<FollowingResponseDto> followedResponseDtoList = new ArrayList<>();
//        for(Follow follow : followList) {
//            followedResponseDtoList.add(new FollowingResponseDto(follow.getFollower().getId(),follow.getFollower().getNickname()));
//        }
//        return new ResponseEntity<>(Message.success(followedResponseDtoList), HttpStatus.OK);
//    }
//    @Transactional(readOnly = true)
//    public ResponseEntity<?> getMemberByAllFoloowing(String nickname , HttpServletRequest request){
//        validateMember(request);
//        Member member = isPresentMemberFollow(nickname);
//        List<Follow> followList = followRepository.findAllByFollower(member);
//        List<FollowedResponseDto> followedResponseDtoList = new ArrayList<>();
//        for(Follow follow : followList) {
//            followedResponseDtoList.add(new FollowedResponseDto(follw))
//        }
//    }
    public Member validateMember(HttpServletRequest request) {
        if(!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public void checkAccessToken (HttpServletRequest request, Member member){
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7)))
            throw new CustomException(ErrorCode.TOKEN_IS_EXPIRED);
        if (null == member) throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
    }
    public Member isPresentMemberFollow(String  nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }
}






