package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.service.LikeService;
import com.example.rhythme_backend.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/member/post/{singerpostId}/like")
    public ResponseDto<?> SingerLike(@PathVariable Long singerpostId, HttpServletRequest request){
        return likeService.upDownSingerLike(singerpostId, request);
    }

    @PostMapping("/member/post/{makerpostId}/like")
    public ResponseDto<?> MakerLike(@PathVariable Long makerpostId, HttpServletRequest request){
        return likeService.upDownMakerLike(makerpostId, request);
    }





}
