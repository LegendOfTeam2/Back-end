package com.example.rhythme_backend.controller;


import com.example.rhythme_backend.domain.Message;
import com.example.rhythme_backend.dto.requestDto.profile.ModifyProfileRequestDto;
import com.example.rhythme_backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ProfilePageController {

    private final ProfileService profileService;



    @GetMapping("/profile/{nickname}")
    public ResponseEntity<?> profileGetOne(@PathVariable String nickname){
        return new ResponseEntity<>(Message.success(profileService.profileGetOne(nickname)), HttpStatus.OK);
    }

    @GetMapping("/post/upload/{nickname}")
    public ResponseEntity<?> profileGetMyUpload(@PathVariable String nickname
                                                ,
                                                @PageableDefault(size = 6 , sort ="id", direction = Sort.Direction.DESC)
                                                    Pageable pageable
                                                )
    {
        return new ResponseEntity<>(Message.success(profileService.profileGetMyUpload(nickname,pageable)),HttpStatus.OK
        );
    }

    @GetMapping("/post/like/{nickname}")
    public ResponseEntity<?> profileGetMyLike(@PathVariable String nickname){
        return new ResponseEntity<>(Message.success(profileService.profileGetMyLike(nickname)),HttpStatus.OK);
    }

    @PutMapping("/profile/{nickname}")
    public ResponseEntity<?> profileChange(@PathVariable String nickname,@RequestBody ModifyProfileRequestDto requestDto){
        return profileService.profileModify(nickname,requestDto);
    }
}
