package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.Follow;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.like.MakerLike;
import com.example.rhythme_backend.domain.like.SingerLike;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.MakerPostTag;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.domain.post.SingerPostTag;
import com.example.rhythme_backend.dto.responseDto.DetailResponseDto;
import com.example.rhythme_backend.dto.responseDto.MyImageResponseDto;
import com.example.rhythme_backend.dto.responseDto.mainpage.*;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.FollowRepository;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.repository.like.MakerLikeRepository;
import com.example.rhythme_backend.repository.like.SingerLikeRepository;
import com.example.rhythme_backend.repository.posts.MakerPostRepository;
import com.example.rhythme_backend.repository.posts.MakerPostTagRepository;
import com.example.rhythme_backend.repository.posts.SingerPostRepository;
import com.example.rhythme_backend.repository.posts.SingerPostTagRepository;
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
    private final MakerPostTagRepository makerPostTagRepository;
    private final SingerPostTagRepository singerPostTagRepository;

    public ResponseEntity<?> bestSong() {
        List<BestSongResponseDto> bestSongResponseDtoList = new ArrayList<>();
        List<MakerPost> makerPostList = makerPostRepository.findTopByOrderByLikesDesc();
        List<SingerPost> singerPostList = singerPostRepository.findTopByOrderByLikesDesc();

        if (makerPostList.size() == 0 && singerPostList.size() == 0) {
            return new ResponseEntity<>(Message.fail("POST_NOT_FOUND","해당되는 게시글이 없습니다."),HttpStatus.NOT_FOUND);
        }

        if (makerPostList.size() == 0) {
            for (SingerPost singerPost : singerPostList) {
                bestSongResponseDtoList.add(BestSongResponseDto.builder()
                                .postId(singerPost.getId())
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

        if (singerPostList.size() == 0) {
            for (MakerPost makerPost : makerPostList) {
                bestSongResponseDtoList.add(BestSongResponseDto.builder()
                                .postId(makerPost.getId())
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

        if(makerPostList.get(0).getLikes() >= singerPostList.get(0).getLikes()) {
            for (MakerPost makerPost : makerPostList) {
                bestSongResponseDtoList.add(BestSongResponseDto.builder()
                                .postId(makerPost.getId())
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
                            .postId(singerPost.getId())
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
        List<MakerPost> makerPostList = makerPostRepository.findTop30ByOrderByCreatedAtDesc();
        List<RecentMakerResponseDto> recentMakerResponseDtoList = new ArrayList<>();
        for (MakerPost makerPost : makerPostList) {
            recentMakerResponseDtoList.add(RecentMakerResponseDto.builder()
                            .postId(makerPost.getId())
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
        List<SingerPost> singerPostList = singerPostRepository.findTop30ByOrderByCreatedAtDesc();
        List<RecentSingerResponseDto> recentSingerResponseDtoList = new ArrayList<>();
        for (SingerPost singerPost : singerPostList) {
            recentSingerResponseDtoList.add(RecentSingerResponseDto.builder()
                            .postId(singerPost.getId())
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
        List<MakerPost> makerPostList = makerPostRepository.findTop30ByOrderByLikesDesc();
        List<BestMakerResponseDto> bestMakerResponseDtoList = new ArrayList<>();
        for (MakerPost makerPost : makerPostList) {
            bestMakerResponseDtoList.add(BestMakerResponseDto.builder()
                            .postId(makerPost.getId())
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
        List<SingerPost> singerPostList = singerPostRepository.findTop30ByOrderByLikesDesc();
        List<BestSingerResponseDto> bestSingerResponseDtoList = new ArrayList<>();
        for (SingerPost singerPost : singerPostList) {
            bestSingerResponseDtoList.add(BestSingerResponseDto.builder()
                            .postId(singerPost.getId())
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
        List<Member> memberList = memberRepository.findTop30ByOrderByFollowersDesc();
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
        List<MakerLike> makerLikeList = makerLikeRepository.findAllByMemberIdOrderByMakerPost(member);
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
        List<SingerLike> singerLikeList = singerLikeRepository.findAllByMemberIdOrderBySingerPost(member);
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

    public ResponseEntity<?> getMyImage(HttpServletRequest request) {
        Member member = validateMember(request);
        Member optionalMember = memberRepository.findByNickname(member.getNickname()).orElseGet(Member::new);
        MyImageResponseDto myImageResponseDto = MyImageResponseDto.builder()
                .imgUrl(optionalMember.getImageUrl())
                .build();
        return new ResponseEntity<>(Message.success(myImageResponseDto),HttpStatus.OK);
    }

    public ResponseEntity<?> getDetailPage(Long postId, String position) {
        MakerPost makerPost = makerPostRepository.findById(postId).orElseGet(MakerPost::new);
        SingerPost singerPost = singerPostRepository.findById(postId).orElseGet(SingerPost::new);
        List<String> tagResponseList = new ArrayList<>();

        if(position.equals("Maker")) {
            List<MakerPostTag> makerTagList = makerPostTagRepository.findAllByMakerPostId(makerPost);
            for (MakerPostTag tag: makerTagList) {
                tagResponseList.add(tag.getTagId().getTag());
            }
            DetailResponseDto detailResponseDto = DetailResponseDto.builder()
                    .postId(makerPost.getId())
                    .position("Maker")
                    .memberImageUrl(makerPost.getMember().getImageUrl())
                    .title(makerPost.getTitle())
                    .content(makerPost.getContent())
                    .nickname(makerPost.getMember().getNickname())
                    .lyrics(makerPost.getLyrics())
                    .imageUrl(makerPost.getImageUrl().getImageUrl())
                    .mediaUrl(makerPost.getMediaUrl().getMediaUrl())
                    .createdAt(makerPost.getCreatedAt())
                    .modifiedAt(makerPost.getModifiedAt())
                    .likes(makerPost.getLikes())
                    .collaborate(makerPost.getCollaborate())
                    .tags(tagResponseList)
                    .build();

            return new ResponseEntity<>(Message.success(detailResponseDto),HttpStatus.OK);
        }
        List<SingerPostTag> singerPostTagList = singerPostTagRepository.findAllBySingerPostId(singerPost);
        for (SingerPostTag tag: singerPostTagList) {
            tagResponseList.add(tag.getTagId().getTag());
        }
        if(position.equals("Singer")) {
            DetailResponseDto detailResponseDto = DetailResponseDto.builder()
                    .postId(singerPost.getId())
                    .position("Singer")
                    .memberImageUrl(singerPost.getMember().getImageUrl())
                    .title(singerPost.getTitle())
                    .content(singerPost.getContent())
                    .nickname(singerPost.getMember().getNickname())
                    .lyrics(singerPost.getLyrics())
                    .imageUrl(singerPost.getImageUrl().getImageUrl())
                    .mediaUrl(singerPost.getMediaUrl().getMediaUrl())
                    .createdAt(singerPost.getCreatedAt())
                    .modifiedAt(singerPost.getModifiedAt())
                    .likes(singerPost.getLikes())
                    .collaborate(singerPost.getCollaborate())
                    .tags(tagResponseList)
                    .build();

            return new ResponseEntity<>(Message.success(detailResponseDto),HttpStatus.OK);
        }
        return new ResponseEntity<>(Message.fail("POSITION_NOT_FOUND","리드미에서 지원하지 않는 포지션입니다."),HttpStatus.OK);
    }
    
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
    
}
