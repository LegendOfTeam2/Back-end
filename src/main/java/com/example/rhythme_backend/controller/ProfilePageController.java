package com.example.rhythme_backend.controller;


import com.example.rhythme_backend.domain.Message;
import com.example.rhythme_backend.dto.requestDto.profile.ModifyProfileRequestDto;
import com.example.rhythme_backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequiredArgsConstructor
public class ProfilePageController {

    private final ProfileService profileService;



    @GetMapping("/profile/{nickname}")
    public ResponseEntity<?> profileGetOne(@PathVariable String nickname){
        return new ResponseEntity<>(Message.success(profileService.profileGetOne(nickname)), HttpStatus.OK);
    }

    @GetMapping("/post/upload/{nickname}")
    public ResponseEntity<?> profileGetMyUpload(@PathVariable String nickname) {
        return profileService.profileGetMyUpload(nickname);
    }

    @GetMapping("/post/like/{nickname}")
    public ResponseEntity<?> profileGetMyLike(@PathVariable String nickname) {
        return profileService.profileGetMyLike(nickname);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> profileChange(@RequestBody ModifyProfileRequestDto requestDto, HttpServletResponse response, HttpServletRequest request){
        return profileService.profileModify(requestDto,response,request);
    }

}
