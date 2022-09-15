package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.dto.requestDto.post.PostCreateRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 카테고리별 게시판 전체 조회
    @GetMapping("/auth/makerpost")
    public ResponseEntity<?> getMakerPost() {
        return postService.getAllMakerPost();
    }

    @GetMapping("/auth/singerpost")
    public ResponseEntity<?> getSingerPost() {
        return postService.getAllSingerPost();
    }


    // 글 쓰기 API
    @PostMapping("/auth/post")
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto) {
        return postService.createPost(postCreateRequestDto);
    }

    //글 수정 API
    @PutMapping("/auth/post")
    public ResponseEntity<?> patchPost(@RequestBody PostPatchRequestDto postPatchRequestDto) {
        return postService.patchPost(postPatchRequestDto);
    }

    // 글삭제 API
    @DeleteMapping("/auth/post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestParam String position) {
        return postService.deletePost(postId,position);
    }

    @GetMapping("/allpost/search")
    public ResponseEntity<?> searchAllPost(@RequestParam(required = false, defaultValue = "") String searchText,
                                           @RequestParam String category) {
        return postService.AllPostSearch(searchText,category);
    }
}