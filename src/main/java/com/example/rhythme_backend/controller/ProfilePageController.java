package com.example.rhythme_backend.controller;


import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.repository.posts.MakerPostRepository;
import com.example.rhythme_backend.repository.posts.SingerPostRepository;
import com.example.rhythme_backend.service.ProfileService;
import com.example.rhythme_backend.util.Message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ProfilePageController {

    private final ProfileService profileService;



    @GetMapping("/profile/{nickname}")
    public ResponseEntity<?> profileGetOne(@PathVariable String nickname){
        return new ResponseEntity<>(Message.success(
                profileService.profileGetOne(nickname)
        ), HttpStatus.OK);
    }

    @GetMapping("/post/upload/{nickname}")
    public ResponseEntity<?> profileGetMyUpload(@PathVariable String nickname){
        return new ResponseEntity<>(Message.success(
                profileService.profileGetMyUpload(nickname)),HttpStatus.OK
        );
    }

    @GetMapping("/post/like/{nickname}")
    public ResponseEntity<?> profileGetMyLike(@PathVariable String nickname){
        return new ResponseEntity<>(Message.success(
                profileService.profileGetMyLike(nickname)
        ),HttpStatus.OK);
    }
}
