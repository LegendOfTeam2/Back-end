package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.Follow;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.like.MakerLike;
import com.example.rhythme_backend.domain.like.SingerLike;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.responseDto.mainpage.*;
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
import java.util.Optional;

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
        if(makerPostList.size() >= singerPostList.size()) {
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

                bestSongResponseDtoList.add(BestSongResponseDto.builder()
                                .position("Singer")
                                .collaborate(singerPostList.get().getCollaborate())
                                .imageUrl(singerPostList.get().getImageUrl())
                                .title(singerPostList.get().getTitle())
                                .likes(singerPostList.get().getLikes())
                                .mediaUrl(singerPostList.get().getMediaUrl())
                                .nickname(singerPostList.get().getMember().getNickname())
                                .content(singerPostList.get().getContent())
                                .build());

            return new ResponseEntity<>(Message.success(bestSongResponseDtoList), HttpStatus.OK);
    }

    public ResponseEntity<?> recentMaker() {
        List<MakerPost> makerPostList = makerPostRepository.findAllByOrderByLikesDesc();
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
        List<SingerPost> singerPostList = singerPostRepository.findTop10ByOrderByCreatedAt();
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
        List<SingerPost> singerPostList = singerPostRepository.findTop10ByOrderByLikesDesc();
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
        List<Member> memberList = memberRepository.findTop10ByOrderByFollowersDesc();
        List<PowerArtistResponseDto> powerArtistResponseDtoList = new ArrayList<>();
        for (Member member : memberList) {
            powerArtistResponseDtoList.add(PowerArtistResponseDto.builder()
                            .nickname(member.getNickname())
                            .imageUrl(member.getImageUrl())
                            .follower(member.getFollowers())
                            .build());
        }
        return new ResponseEntity<>(Message.success(powerArtistResponseDtoList),HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> makerLikeList(HttpServletRequest request) {
        Member member = validateMember(request);
        List<MakerLike> makerLikeList = makerLikeRepository.findAllByMemberOrderByMakerPost(member);
        List<MyMakerResponseDto> myMakerResponseDtoList = new ArrayList<>();
        for (MakerLike makerLike : makerLikeList) {
            myMakerResponseDtoList.add(MyMakerResponseDto.builder()
                            .makerId(makerLike.getMakerPost().getId())
                            .build());
        }
        return new ResponseEntity<>(Message.success(myMakerResponseDtoList),HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> singerLikeList(HttpServletRequest request) {
        Member member = validateMember(request);
        List<SingerLike> singerLikeList = singerLikeRepository.findAllByMemberOrderBySingerPost(member);
        List<MySingerResponseDto> mySingerResponseDtoList = new ArrayList<>();
        for (SingerLike singerLike : singerLikeList) {
            mySingerResponseDtoList.add(MySingerResponseDto.builder()
                            .singerId(singerLike.getSingerPost().getId())
                            .build());
        }
        return new ResponseEntity<>(Message.success(mySingerResponseDtoList),HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> followList(HttpServletRequest request) {
        Member member = validateMember(request);
        List<Follow> followList = followRepository.findAllByFollowerOrderByFollowing(member);
        List<MyArtistResponseDto> myArtistResponseDtoList = new ArrayList<>();
        for (Follow follow : followList) {
            myArtistResponseDtoList.add(MyArtistResponseDto.builder()
                            .followingId(follow.getFollowing().getId())
                            .build());
        }
        return new ResponseEntity<>(Message.success(myArtistResponseDtoList),HttpStatus.OK);
    }

    
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
