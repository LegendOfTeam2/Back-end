package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.service.LikeService;
import com.example.rhythme_backend.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class LikeController {
    private final LikeService likeService;

    @GetMapping("/post/{postId}/like/{position}")
    public ResponseDto<?> SingerLike(@PathVariable Long postId, HttpServletRequest request,
                                    @PathVariable String position){
        if(position.equals("Singer")){
            return likeService.upDownSingerLike(postId,request);
        }
        if(position.equals("Maker")){
            return likeService.upDownMakerLike(postId,request);
        }
        return ResponseDto.success(true);
        }

}
