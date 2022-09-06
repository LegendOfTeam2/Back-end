package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.service.FollowService;
import com.example.rhythme_backend.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/auth/follow/{nickname}")
    public ResponseDto<?> follow(@PathVariable String nickname, HttpServletRequest request) {
        return followService.upDownFollow(nickname, request);
    }

//    @GetMapping("/followers")
//    public ResponseEntity<?> AllFollowers(@PathVariable String nickname, HttpServletRequest request) {
//        return followService.getFollowers(nickname, request);
//    }
//    @GetMapping("/followings")
//    public ResponseEntity<?> AllFollowings(@PathVariable String nickname, HttpServletRequest request) {
//        return followService.getFollowings(nickname, request);
//    }
}
