package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.domain.Message;
import com.example.rhythme_backend.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class LikeController {
    private final LikeService likeService;

    @GetMapping("/post/{postId}/like/{position}")
    public ResponseEntity<?> PostLike(@PathVariable Long postId, HttpServletRequest request,
                                        @PathVariable String position){
        if(position.equals("Singer")){
            return likeService.upDownSingerLike(postId,request);
        }
        if(position.equals("Maker")){
            return likeService.upDownMakerLike(postId,request);
        }
        return new ResponseEntity<>(Message.success(true), HttpStatus.BAD_REQUEST);
        }

}
