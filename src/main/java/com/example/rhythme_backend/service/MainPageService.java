package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.media.MediaUrl;
import com.example.rhythme_backend.dto.responseDto.BestSongResponseDto;
import com.example.rhythme_backend.repository.posts.ImageUrlRepository;
import com.example.rhythme_backend.repository.posts.MediaUrlRepository;
import com.example.rhythme_backend.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MediaUrlRepository mediaUrlRepository;

//    public ResponseEntity<?> bestSong() {
//        List<MediaUrl> mediaUrlList = mediaUrlRepository.findById();
//        List<BestSongResponseDto> bestSongResponseDtoList = new ArrayList<>();
//        for (MediaUrl mediaUrl : mediaUrlList) {
//        bestSongResponseDtoList.add(BestSongResponseDto.builder()
//                .id(mediaUrl.getId())
//                .mediaUrl(mediaUrl.getMediaUrl())
//                .build());
//        }
//        return new ResponseEntity<>(Message.success(bestSongResponseDtoList), HttpStatus.OK);
//    }

}
