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

    @PostMapping("/member/follow/{memberId}")
    public ResponseDto<?> follow(@PathVariable Long memberId, HttpServletRequest request) {
        return followService.upDownFollow(memberId, request);
    }
}
