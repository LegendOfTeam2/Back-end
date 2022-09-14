package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.dto.requestDto.MyImageRequestDto;
import com.example.rhythme_backend.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/api/bestsong")
    public ResponseEntity<?> getBestSong() {
        return mainPageService.bestSong();
    }

    @GetMapping("/api/recentmaker")
    public ResponseEntity<?> getRecentMaker() {
        return mainPageService.recentMaker();
    }

    @GetMapping("/api/recentsinger")
    public ResponseEntity<?> getRecentSinger() {
        return mainPageService.recentSinger();
    }

    @GetMapping("/api/bestmaker")
    public ResponseEntity<?> getBestMaker() {
        return mainPageService.bestMaker();
    }

    @GetMapping("/api/bestsinger")
    public ResponseEntity<?> getBestSinger() {
        return mainPageService.bestSinger();
    }

    @GetMapping("/api/powerartist")
    public ResponseEntity<?> getPowerArtist() {
        return mainPageService.MostLikeArtist();
    }

    @GetMapping("/auth/makerlikepost")
    public ResponseEntity<?> makerLikePost(HttpServletRequest request){
        return mainPageService.makerLikeList(request);
    }

    @GetMapping("/auth/singerlikepost")
    public ResponseEntity<?> singerLikePost(HttpServletRequest request){
        return mainPageService.singerLikeList(request);
    }

    @GetMapping("/auth/followerlist")
    public ResponseEntity<?> followList(HttpServletRequest request){
        return mainPageService.followList(request);
    }

    @GetMapping("/auth/myimage")
    public ResponseEntity<?> getMyImage(HttpServletRequest request, @RequestBody MyImageRequestDto requestDto) {
        return mainPageService.getMyImage(request,requestDto);
    }

    @GetMapping("api/post/{postId}")
    public ResponseEntity<?> detailPage(@PathVariable Long postId, @RequestParam String position) {
        return mainPageService.getDetailPage(postId,position);
    }

}
