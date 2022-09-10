package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.dto.requestDto.post.PostCreateRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostDeleteRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 카테고리별 게시판 전체 조회
    @GetMapping("/auth/makerpost")
    public ResponseEntity<?> getMakerPost(){
      return postService.getAllMakerPost();
    }

    @GetMapping("/auth/singerpost")
    public ResponseEntity<?> getSingerPost(){
        return postService.getAllSingerPost();
    }


    // 글 쓰기 API
    @PostMapping("/auth/post")
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto){
        return postService.createPost(postCreateRequestDto);
    }

    //글 수정 API
    @PatchMapping("/auth/post")
    public ResponseEntity<?> patchPost(@RequestBody PostPatchRequestDto postPatchRequestDto){
        return postService.patchPost(postPatchRequestDto);
    }

    // 글삭제 API
    @DeleteMapping("/auth/post")
    public ResponseEntity<?> deletePost(@RequestBody PostDeleteRequestDto postDeleteRequestDto){
        return postService.deletePost(postDeleteRequestDto);
    }
    // 메이커게시물 페이징 및 검색
//    @GetMapping("/makerpost/search")
//    public ResponseEntity<?> pagemakerpost(Model model,
//                       @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable page,
//                       @RequestParam(required = false, defaultValue = "") String searchText) {
//
//        return postService.searchmakerposts(model,page,searchText);
//    }
    @GetMapping("/mypage/makerpost")
    public ResponseEntity<?> makerpostss(Model model,
                                           @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC)
                                           Pageable page) {
        return postService.makerposts(model,page);
    }
}
