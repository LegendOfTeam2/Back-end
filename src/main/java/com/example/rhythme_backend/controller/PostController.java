package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.dto.requestDto.PostRequestDto;
import com.example.rhythme_backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 카테고리별 게시판 전체 조회
    @GetMapping("/auth/makerpost")
    public ResponseEntity<?> getMakerPost(){
      return postService.getAllSingerPost();
    }

    @GetMapping("/auth/singerpost")
    public ResponseEntity<?> getSingerPost(){
        return postService.getAllMakerPost();
    }



    // 카테고리별 게시판 글 쓰기
    @PostMapping("/auth/makerpost")
    public ResponseEntity<?> createMakerPost(@RequestBody PostRequestDto postRequestDto){
        return postService.createMakerPost(postRequestDto);
    }

    @PostMapping("/auth/singerpost")
    public ResponseEntity<?> createSingerPost(@RequestBody PostRequestDto postRequestDto){
        return postService.createSingerPost(postRequestDto);
    }
}
