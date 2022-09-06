package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.media.MediaUrl;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.responseDto.*;
import com.example.rhythme_backend.repository.media.MediaUrlRepository;
import com.example.rhythme_backend.repository.posts.MakerPostRepository;
import com.example.rhythme_backend.repository.posts.SingerPostRepository;
import com.example.rhythme_backend.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MediaUrlRepository mediaUrlRepository;
    private final MakerPostRepository makerPostRepository;
    private final SingerPostRepository singerPostRepository;

    public ResponseEntity<?> bestSong() {
        Optional<MediaUrl> optionalMediaUrl = makerPostRepository.;
        return new ResponseEntity<>(Message.success(optionalMediaUrl), HttpStatus.OK);
    }

    public ResponseEntity<?> recentMaker() {
        List<MakerPost> makerPostList = makerPostRepository.countAllByOrderByCreatedAt();
        List<RecentMakerResponseDto> recentMakerResponseDtoList = new ArrayList<>();
        for (MakerPost makerPost : makerPostList) {
            recentMakerResponseDtoList.add(RecentMakerResponseDto.builder()
                            .mediaUrl(makerPost.getMediaUrl())
                            .build());
        }
        return new ResponseEntity<>(Message.success(recentMakerResponseDtoList),HttpStatus.OK);
    }

    public ResponseEntity<?> recentSinger() {
        List<SingerPost> singerPostList = singerPostRepository.countAllByOrderByCreatedAt();
        List<RecentSingerResponseDto> recentSingerResponseDtoList = new ArrayList<>();
        for (SingerPost singerPost : singerPostList) {
            recentSingerResponseDtoList.add(RecentSingerResponseDto.builder()
                            .mediaUrl(singerPost.getMediaUrl())
                            .build());
        }
        return new ResponseEntity<>(Message.success(recentSingerResponseDtoList),HttpStatus.OK);
    }

    public ResponseEntity<?> bestMaker() {
        List<MakerPost> makerPostList = makerPostRepository.findById();
        List<BestMakerResponseDto> bestMakerResponseDtoList = new ArrayList<>();
        for (MakerPost makerPost : makerPostList) {
            bestMakerResponseDtoList.add(BestMakerResponseDto.builder()
                    .build());
        }
        return new ResponseEntity<>(Message.success(bestMakerResponseDtoList),HttpStatus.OK);
    }

    public ResponseEntity<?> bestSinger() {
        List<SingerPost> singerPostList = singerPostRepository.findById();
        List<BestSingerResponseDto> bestSingerResponseDtoList = new ArrayList<>();
        for (SingerPost singerPost : singerPostList) {
            bestSingerResponseDtoList.add(BestSingerResponseDto.builder()
                    .build());
        }
        return new ResponseEntity<>(Message.success(bestSingerResponseDtoList),HttpStatus.OK);
    }

}
