package com.example.rhythme_backend.controller;

import com.example.rhythme_backend.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

//    @GetMapping("/api/mainpage")
//    public ResponseEntity<?> getBestSong() {
//        return mainPageService.bestSong();
//    }

}
