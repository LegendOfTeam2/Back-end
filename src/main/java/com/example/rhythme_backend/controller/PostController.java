package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.dto.requestDto.PageRequest;
import com.example.rhythme_backend.dto.requestDto.post.PostCreateRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostDeleteRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.service.PostService;
import com.example.rhythme_backend.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PageRequest pageRequest;

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
    @PatchMapping("/auth/post")
    public ResponseEntity<?> patchPost(@RequestBody PostPatchRequestDto postPatchRequestDto) {
        return postService.patchPost(postPatchRequestDto);
    }

    // 글삭제 API
    @DeleteMapping("/auth/post")
    public ResponseEntity<?> deletePost(@RequestBody PostDeleteRequestDto postDeleteRequestDto) {
        return postService.deletePost(postDeleteRequestDto);
    }

    @GetMapping("/makerpost/search")
    public ResponseEntity<?> getSearchMakerPost(@RequestParam(required = false, defaultValue = "") String searchText) {
        return postService.getAllMakerPostSearch(searchText);
    }

    @GetMapping("/singerpost/search")
    public ResponseEntity<?> getSearchSingerPost(@RequestParam(required = false, defaultValue = "") String searchText) {
        return postService.getAllSingerPostSearch(searchText);
    }

    @GetMapping("/allpost/search")
    public ResponseEntity<?> searchAllPost(@RequestParam(required = false, defaultValue = "") String searchText,
                                           @RequestParam String category) {
        if (category.equals("Singer")) {
            return postService.getAllSingerPostSearch(searchText);

        } else if (category.equals("Maker")) {
            return postService.getAllMakerPostSearch(searchText);
        }
    return new ResponseEntity<>(Message.success("검색어를 입력 해주세요"), HttpStatus.OK);
    }
}