package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/auth/follow/{nickname}")
    public ResponseEntity<?> follow(@PathVariable String nickname, HttpServletRequest request) {
        return followService.upDownFollow(nickname, request);
    }

//    @GetMapping("/auth/followers/{nickname}")
//    public ResponseEntity<?> AllFollowerlist(@PathVariable String nickname, HttpServletRequest request) {
//        return followService.getMemberByAllFollower(nickname, request);
//    }

//    @GetMapping("/auth/followings/{nickname}")
//    public ResponseEntity<?> AllFollowinglist(@PathVariable String nickname, HttpServletRequest request) {
//        return followService.getMemberByFollowings(nickname, request);
//    }
}
