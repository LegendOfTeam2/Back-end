package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.HashTag;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.like.MakerLike;
import com.example.rhythme_backend.domain.like.SingerLike;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.requestDto.post.PostCreateRequestDto;
import com.example.rhythme_backend.dto.requestDto.profile.ModifyProfileRequestDto;
import com.example.rhythme_backend.dto.responseDto.profile.ProfileResponseDto;
import com.example.rhythme_backend.dto.responseDto.profile.ProfileUploadPostResponseDto;
import com.example.rhythme_backend.repository.FollowRepository;
import com.example.rhythme_backend.repository.HashTagRepository;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.repository.like.MakerLikeRepository;
import com.example.rhythme_backend.repository.like.SingerLikeRepository;
import com.example.rhythme_backend.repository.media.ImageUrlRepository;
import com.example.rhythme_backend.repository.posts.MakerPostRepository;
import com.example.rhythme_backend.repository.posts.SingerPostRepository;
import com.example.rhythme_backend.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;

    private final MakerPostRepository makerPostRepository;

    private final SingerPostRepository singerPostRepository;

    private final MakerLikeRepository makerLikeRepository;

    private final SingerLikeRepository singerLikeRepository;

    private final ImageUrlRepository imageUrlRepository;
    private final FollowRepository followRepository;


    private final HashTagRepository hashTagRepository;


    public ProfileResponseDto profileGetOne(String nickname){
        //팔로워 팔로잉은 따로 API 있음
        Member member = memberRepository.findByNickname(nickname).orElseGet(Member::new);
        Long follower = followRepository.countByFollower(member);
        Long following = followRepository.countByFollowing(member);
        Long makerPostCnt = makerPostRepository.countByMember(member);
        List<HashTag> HashTagList = hashTagRepository.findAllByMember(member);
        List<String> stringList = new ArrayList<>();
        for(HashTag a : HashTagList){
            stringList.add(a.getHashtag());
        }

        return ProfileResponseDto.builder()
                .hashtag(stringList)
                .nickname(nickname)
                .imageUrl(member.getImageUrl())
                .myPostConunt(makerPostCnt)
                .follower(follower)
                .following(following)
                .build();

    }

@Transactional
    public ResponseEntity<?> profileModify(String nickname , ModifyProfileRequestDto requestDto) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(
            () -> new IllegalArgumentException("닉네임이 일치하지 않습니다"));
        member.update(requestDto);
        hashTagRepository.deleteByMember(member);
        for(String a : requestDto.getHashtag()){
            HashTag hashTag = HashTag.builder()
                    .hashtag(a)
                    .member(member)
                    .build();
            hashTagRepository.save(hashTag);
        }
    return new ResponseEntity<>(Message.success(ModifyProfileRequestDto.builder()
            .nickname(requestDto.getNickname())
            .imageUrl(requestDto.getImageUrl())
            .introduce(requestDto.getIntroduce())
            .hashtag(requestDto.getHashtag())
            .build()),
            HttpStatus.OK);
}

    public List<ProfileUploadPostResponseDto> profileGetMyUpload(String nickname) {
        Member member = memberRepository.findByNickname(nickname).orElseGet(Member::new);
        List<MakerPost> makerPostList = makerPostRepository.findAllByMember(member);
        List<SingerPost> singerPostList = singerPostRepository.findAllByMember(member);
        List<ProfileUploadPostResponseDto> answer = new ArrayList<>();
        for (MakerPost a : makerPostList) {
            answer.add(ProfileUploadPostResponseDto.builder()
                    .postId(a.getId())
                    .position("Maker")
                    .title(a.getTitle())
                    .nickname(a.getMember().getNickname())
                    .imageUrl(a.getImageUrl().getImageUrl())
                    .mediaUrl(a.getMediaUrl().getMediaUrl())
                    .likeCount(a.getLikes())
                    .collaborate(a.getCollaborate())
                    .createdAt(a.getCreatedAt())
                    .modifiedAt(a.getModifiedAt())
                    .build());
            return answer;

        }
        for (SingerPost a : singerPostList) {
            answer.add(ProfileUploadPostResponseDto.builder()
                    .postId(a.getId())
                    .position("Singer")
                    .title(a.getTitle())
                    .nickname(a.getMember().getNickname())
                    .imageUrl(a.getImageUrl().getImageUrl())
                    .mediaUrl(a.getMediaUrl().getMediaUrl())
                    .likeCount(a.getLikes())
                    .collaborate(a.getCollaborate())
                    .createdAt(a.getCreatedAt())
                    .modifiedAt(a.getModifiedAt())
                    .build());
            return answer;
        }
        return answer;
    }

    public List<ProfileUploadPostResponseDto> profileGetMyLike(String nickname){
        List<ProfileUploadPostResponseDto> answer = new ArrayList<>();
        Member member = memberRepository.findByNickname(nickname).orElseGet(Member::new);
        List<MakerLike> makerLikeList = makerLikeRepository.findByMemberId(member);
        List<SingerLike> singerLikeList = singerLikeRepository.findByMemberId(member);
        for (MakerLike a : makerLikeList) {
            MakerPost exportFromA = a.getMakerPost();
            answer.add(ProfileUploadPostResponseDto.builder()
                    .postId(exportFromA.getId())
                    .position("Maker")
                    .title(exportFromA.getTitle())
                    .nickname(exportFromA.getMember().getNickname())
                    .imageUrl(exportFromA.getImageUrl().getImageUrl())
                    .mediaUrl(exportFromA.getMediaUrl().getMediaUrl())
                    .likeCount(exportFromA.getLikes())
                    .collaborate(exportFromA.getCollaborate())
                    .createdAt(exportFromA.getCreatedAt())
                    .modifiedAt(exportFromA.getModifiedAt())
                    .build());
            return answer;

        }
        for (SingerLike a : singerLikeList) {
            SingerPost exportFromA = a.getSingerPost();
            answer.add(ProfileUploadPostResponseDto.builder()
                    .postId(exportFromA.getId())
                    .position("Singer")
                    .title(exportFromA.getTitle())
                    .nickname(exportFromA.getMember().getNickname())
                    .imageUrl(exportFromA.getImageUrl().getImageUrl())
                    .mediaUrl(exportFromA.getMediaUrl().getMediaUrl())
                    .likeCount(exportFromA.getLikes())
                    .collaborate(exportFromA.getCollaborate())
                    .createdAt(exportFromA.getCreatedAt())
                    .modifiedAt(exportFromA.getModifiedAt())
                    .build());
            return answer;
        }

        return answer;
    }
    public ImageUrl imageUrlSave(PostCreateRequestDto postCreateRequestDto){
        ImageUrl imageUrl =  ImageUrl.builder()
                .postId(null)
                .position(postCreateRequestDto.getPosition())
                .imageUrl(postCreateRequestDto.getImageUrl())
                .build();

        imageUrlRepository.save(imageUrl);
        return imageUrl;
    }

}
