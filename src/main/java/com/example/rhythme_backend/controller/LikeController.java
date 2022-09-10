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

    @PostMapping("/auth/singerpost/{postId}/like")
    public ResponseDto<?> SingerLike(@PathVariable Long postId, HttpServletRequest request){
        return likeService.upDownSingerLike(postId, request);
    }

    @PostMapping("/auth/makerpost/{postId}/like")
    public ResponseDto<?> MakerLike(@PathVariable Long postId, HttpServletRequest request){
        return likeService.upDownMakerLike(postId, request);
    }

}
