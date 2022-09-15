package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.service.LikeService;
import com.example.rhythme_backend.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @GetMapping("/auth/post/{postId}/like/{position}")
    public ResponseDto<?> SingerLike(@PathVariable Long postId, HttpServletRequest request,
                                    @PathVariable String position){
        if(position.equals("Singer")){
            return likeService.upDownSingerLike(postId,request);
        }
        if(position.equals("Maker")){
            return likeService.upDownMakerLike(postId,request);
        }
        return ResponseDto.success("잘못된 접근입니다");
        }

    @PostMapping("/auth/makerpost/{postId}/like")
    public ResponseDto<?> MakerLike(@PathVariable Long postId, HttpServletRequest request){
        return likeService.upDownMakerLike(postId, request);
    }

}
