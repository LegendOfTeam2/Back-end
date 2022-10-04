package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.Follow;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.Message;
import com.example.rhythme_backend.dto.requestDto.FollowRequestDto;
import com.example.rhythme_backend.repository.FollowRepository;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.util.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final Validation validation;

    @Transactional
    public ResponseEntity<?> upDownFollow(String nickname, HttpServletRequest request) {
        Member follower = validation.validateMemberToAccess(request);
        validation.checkAccessToken(request, follower);
        Member following = validation.isPresentMemberFollow(nickname);
        Optional<Follow> findFollowing = followRepository.findByFollowerAndFollowing(follower, following);
        
        if(findFollowing.isEmpty()) {
            FollowRequestDto followRequestDto = new FollowRequestDto(follower, following);
            Follow follow = new Follow(followRequestDto);
            followRepository.save(follow);
            Long followers = followRepository.countAllByFollowingId(following.getId());
            following.updateFollowers(followers);
            memberRepository.save(following);
            return new ResponseEntity<>(Message.success(true), HttpStatus.OK);
        } else {
            followRepository.deleteById(findFollowing.get().getId());
            Long followers = followRepository.countAllByFollowingId(following.getId());
            following.updateFollowers(followers);
            memberRepository.save(following);
            return new ResponseEntity<>(Message.success(false), HttpStatus.OK);
        }
    }

}