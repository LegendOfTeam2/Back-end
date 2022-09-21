package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.dto.requestDto.post.PostCreateRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class PostController {

    private final PostService postService;

    // 카테고리별 게시판 전체 조회
    @GetMapping("/makerpost")
    public ResponseEntity<?> getMakerPost() {
        return postService.getAllMakerPost();
    }

    @GetMapping("/singerpost")
    public ResponseEntity<?> getSingerPost() {
        return postService.getAllSingerPost();
    }


    // 글 쓰기 API
    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto,request);
    }

    //글 수정 API
    @PutMapping("/post")
    public ResponseEntity<?> patchPost(@RequestBody PostPatchRequestDto requestDto,HttpServletRequest request) {
        return postService.patchPost(requestDto,request);
    }

    // 글삭제 API
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestParam String position,HttpServletRequest request) {
        return postService.deletePost(postId,position,request);
    }
}