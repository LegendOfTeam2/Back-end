package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.Follow;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.like.MakerLike;
import com.example.rhythme_backend.domain.like.SingerLike;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.responseDto.*;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.FollowRepository;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.repository.like.MakerLikeRepository;
import com.example.rhythme_backend.repository.like.SingerLikeRepository;
import com.example.rhythme_backend.repository.posts.MakerPostRepository;
import com.example.rhythme_backend.repository.posts.SingerPostRepository;
import com.example.rhythme_backend.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MakerPostRepository makerPostRepository;
    private final SingerPostRepository singerPostRepository;
    private final MemberRepository memberRepository;
    private final MakerLikeRepository makerLikeRepository;
    private final FollowRepository followRepository;
    private final TokenProvider tokenProvider;
    private final SingerLikeRepository singerLikeRepository;

    public ResponseEntity<?> bestSong() {
        List<BestSongResponseDto> bestSongResponseDtoList = new ArrayList<>();
        List<MakerPost> makerPostList = makerPostRepository.findTopByOrderByLikesDesc();
        List<SingerPost> singerPostList = singerPostRepository.findTopByOrderByLikesDesc();
        if(makerPostList.get(0).getLikes() >= singerPostList.get(0).getLikes()) {
            for (MakerPost makerPost : makerPostList) {
                bestSongResponseDtoList.add(BestSongResponseDto.builder()
                                .position("Maker")
                                .collaborate(makerPost.getCollaborate())
                                .imageUrl(makerPost.getImageUrl())
                                .title(makerPost.getTitle())
                                .likes(makerPost.getLikes())
                                .mediaUrl(makerPost.getMediaUrl())
                                .nickname(makerPost.getMember().getNickname())
                                .content(makerPost.getContent())
                                .build());
            }
            return new ResponseEntity<>(Message.success(bestSongResponseDtoList), HttpStatus.OK);
        }
            for (SingerPost singerPost : singerPostList) {
                bestSongResponseDtoList.add(BestSongResponseDto.builder()
                                .position("Singer")
                                .collaborate(singerPost.getCollaborate())
                                .imageUrl(singerPost.getImageUrl())
                                .title(singerPost.getTitle())
                                .likes(singerPost.getLikes())
                                .mediaUrl(singerPost.getMediaUrl())
                                .nickname(singerPost.getMember().getNickname())
                                .content(singerPost.getContent())
                                .build());
        }
            return new ResponseEntity<>(Message.success(bestSongResponseDtoList), HttpStatus.OK);
    }

    public ResponseEntity<?> recentMaker() {
        List<MakerPost> makerPostList = makerPostRepository.findAllByOrderByCreatedAt();
        List<RecentMakerResponseDto> recentMakerResponseDtoList = new ArrayList<>();
        for (MakerPost makerPost : makerPostList) {
            recentMakerResponseDtoList.add(RecentMakerResponseDto.builder()
                            .imageUrl(makerPost.getImageUrl())
                            .collaborate(makerPost.getCollaborate())
                            .position("Maker")
                            .title(makerPost.getTitle())
                            .likes(makerPost.getLikes())
                            .mediaUrl(makerPost.getMediaUrl())
                            .nickname(makerPost.getMember().getNickname())
                            .content(makerPost.getContent())
                            .build());
        }

        return new ResponseEntity<>(Message.success(recentMakerResponseDtoList),HttpStatus.OK);
    }

    public ResponseEntity<?> recentSinger() {
        List<SingerPost> singerPostList = singerPostRepository.findAllByOrderByCreatedAt();
        List<RecentSingerResponseDto> recentSingerResponseDtoList = new ArrayList<>();
        for (SingerPost singerPost : singerPostList) {
            recentSingerResponseDtoList.add(RecentSingerResponseDto.builder()
                            .imageUrl(singerPost.getImageUrl())
                            .collaborate(singerPost.getCollaborate())
                            .position("Singer")
                            .title(singerPost.getTitle())
                            .likes(singerPost.getLikes())
                            .mediaUrl(singerPost.getMediaUrl())
                            .nickname(singerPost.getMember().getNickname())
                            .content(singerPost.getContent())
                            .build());
        }
        return new ResponseEntity<>(Message.success(recentSingerResponseDtoList),HttpStatus.OK);
    }

    public ResponseEntity<?> bestMaker() {
        List<MakerPost> makerPostList = makerPostRepository.findAllByOrderByLikesDesc();
        List<BestMakerResponseDto> bestMakerResponseDtoList = new ArrayList<>();
        for (MakerPost makerPost : makerPostList) {
            bestMakerResponseDtoList.add(BestMakerResponseDto.builder()
                            .imageUrl(makerPost.getImageUrl())
                            .collaborate(makerPost.getCollaborate())
                            .position("Maker")
                            .title(makerPost.getTitle())
                            .likes(makerPost.getLikes())
                            .mediaUrl(makerPost.getMediaUrl())
                            .nickname(makerPost.getMember().getNickname())
                            .content(makerPost.getContent())
                            .build());
        }
        return new ResponseEntity<>(Message.success(bestMakerResponseDtoList),HttpStatus.OK);
    }

    public ResponseEntity<?> bestSinger() {
        List<SingerPost> singerPostList = singerPostRepository.findAllByOrderByLikesDesc();
        List<BestSingerResponseDto> bestSingerResponseDtoList = new ArrayList<>();
        for (SingerPost singerPost : singerPostList) {
            bestSingerResponseDtoList.add(BestSingerResponseDto.builder()
                            .imageUrl(singerPost.getImageUrl())
                            .collaborate(singerPost.getCollaborate())
                            .position("Singer")
                            .title(singerPost.getTitle())
                            .likes(singerPost.getLikes())
                            .mediaUrl(singerPost.getMediaUrl())
                            .nickname(singerPost.getMember().getNickname())
                            .content(singerPost.getContent())
                            .build());
        }
        return new ResponseEntity<>(Message.success(bestSingerResponseDtoList),HttpStatus.OK);
    }

    public ResponseEntity<?> MostLikeArtist() {
        List<Follow> followList = followRepository.findDistinctTop3ByOrderByFollowing();
        List<PowerArtistResponseDto> powerArtistResponseDtoList = new ArrayList<>();
        for (Follow follow : followList) {
            powerArtistResponseDtoList.add(PowerArtistResponseDto.builder()
                    .nickname(follow.getFollowing().getNickname())
                    .build());
        }
        return new ResponseEntity<>(Message.success(powerArtistResponseDtoList),HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public List<Long> makerLikeList(HttpServletRequest request) {
        Member member = validateMember(request);
        List<Long> makerLikeList = new ArrayList<>();
        for (MakerLike makerLike : makerLikeRepository.findAllByMemberIdOrderByMakerPost(member)) {
            makerLikeList.add(makerLike.getMemberId().getId());
        }
        return makerLikeList;
    }

    @Transactional(readOnly = true)
    public List<Long> singerLikeList(HttpServletRequest request) {
        Member member = validateMember(request);
        List<Long> singerLikeList = new ArrayList<>();
        for (SingerLike singerLike : singerLikeRepository.findAllByMemberIdOrderBySingerPost(member)) {
            singerLikeList.add(singerLike.getMemberId().getId());
        }
        return singerLikeList;
    }

    @Transactional(readOnly = true)
    public List<Long> followList(HttpServletRequest request) {
        Member member = validateMember(request);
        List<Long> followerList = new ArrayList<>();
        for (Follow follow : followRepository.findAllByFollowingIdOrderByFollowing(member)) {
            followerList.add(follow.getFollowing().getId());
        }
        return followerList;
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
