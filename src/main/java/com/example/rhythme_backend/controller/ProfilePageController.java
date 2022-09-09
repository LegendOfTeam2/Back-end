package com.example.rhythme_backend.controller;


import com.example.rhythme_backend.domain.like.SingerLike;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.repository.like.MakerLikeRepository;
import com.example.rhythme_backend.repository.like.SingerLikeRepository;
import com.example.rhythme_backend.repository.posts.MakerPostRepository;
import com.example.rhythme_backend.repository.posts.SingerPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfilePageController {

    private final MemberRepository memberRepository;

    private final MakerPostRepository makerPostRepository;

    private final SingerPostRepository singerPostRepository;

    private final MakerLikeRepository makerLikeRepository;

    private final SingerLikeRepository singerLikeRepository;




    @GetMapping("/profile/{nickname}")
    public ResponseEntity<?> profileGetOne(@PathVariable String nickname){

    }
}
