package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public List<Long> makerLikePost(HttpServletRequest request){
        return mainPageService.makerLikeList(request);
    }

    @GetMapping("/auth/singerlikepost")
    public List<Long> singerLikePost(HttpServletRequest request){
        return mainPageService.singerLikeList(request);
    }

    @GetMapping("/auth/followerlist")
    public List<Long> followList(HttpServletRequest request){
        return mainPageService.followList(request);
    }

}
