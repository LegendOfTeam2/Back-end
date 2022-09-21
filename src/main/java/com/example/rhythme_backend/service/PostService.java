package com.example.rhythme_backend.service;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.Message;
import com.example.rhythme_backend.domain.Tag;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.MakerPostTag;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.domain.post.SingerPostTag;
import com.example.rhythme_backend.dto.requestDto.post.PostCreateRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.dto.responseDto.post.*;
import com.example.rhythme_backend.repository.TagRepository;
import com.example.rhythme_backend.repository.like.MakerLikeRepository;
import com.example.rhythme_backend.repository.like.SingerLikeRepository;
import com.example.rhythme_backend.repository.media.ImageUrlRepository;
import com.example.rhythme_backend.repository.media.MediaUrlRepository;
import com.example.rhythme_backend.repository.posts.*;
import com.example.rhythme_backend.util.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService{
    private final SingerPostRepository singerPostRepository;
    private final MakerPostRepository makerPostRepository;
    // 게시글에 달려있는 이미지,태그
    private final TagRepository tagRepository;
    private final ImageUrlRepository imageUrlRepository;
    private final MediaUrlRepository mediaUrlRepository;
    private final MakerPostTagRepository makerPostTagRepository;
    private final SingerPostTagRepository singerPostTagRepository;
    private final MakerLikeRepository makerLikeRepository;
    private final MakerPlayListRepository makerPlayListRepository;
    private final SingerPlayListRepository singerPlayListRepository;
    //AWS S3
    private final S3Service s3Service;
    private final SingerLikeRepository singerLikeRepository;
    private final Validation validation;

    public ResponseEntity<?>  AllPostSearch(String searchText , String category) {
        if (category.equals("Singer")) {
            List<SingerPost> singerPostList = singerPostRepository.findByTitleContainingOrContentContainingOrderByCreatedAtDesc(searchText, searchText);
            List<SearchSingerPostResponseDto> searchSingerPostResponseDtoList = new ArrayList<>();
            for (SingerPost singerPost : singerPostList) {
                searchSingerPostResponseDtoList.add(
                        SearchSingerPostResponseDto.builder()
                                .postId(singerPost.getId())
                                .nickname(singerPost.getMember().getNickname())
                                .title(singerPost.getTitle())
                                .content(singerPost.getContent())
                                .imageUrl(singerPost.getImageUrl().getImageUrl())
                                .mediaUrl(singerPost.getMediaUrl().getMediaUrl())
                                .singerlikeCnt(singerLikeRepository.countAllBySingerPost(singerPost))
                                .collaborate(singerPost.getCollaborate())
                                .build()
                );
            }
            return new ResponseEntity<>(Message.success(searchSingerPostResponseDtoList), HttpStatus.OK);
        }
        if (category.equals("Maker")) {
            List<MakerPost> makerPostList = makerPostRepository.findByTitleContainingOrContentContainingOrderByCreatedAtDesc(searchText, searchText);
            List<SearchMakerPostResponseDto> searchMakerPostResponseDtoList = new ArrayList<>();
            for (MakerPost makerPost : makerPostList) {
                searchMakerPostResponseDtoList.add(
                        SearchMakerPostResponseDto.builder()
                                .postId(makerPost.getId())
                                .nickname(makerPost.getMember().getNickname())
                                .title(makerPost.getTitle())
                                .content(makerPost.getContent())
                                .imageUrl(makerPost.getImageUrl().getImageUrl())
                                .mediaUrl(makerPost.getMediaUrl().getMediaUrl())
                                .makerlikeCnt(makerLikeRepository.countAllByMakerPost(makerPost))
                                .collaborate(makerPost.getCollaborate())
                                .build()
                );
            }
            return new ResponseEntity<>(Message.success(searchMakerPostResponseDtoList), HttpStatus.OK);
        }
     return new ResponseEntity<>(Message.success("잘못된 접근 입니다"), HttpStatus.BAD_REQUEST);
    }

    //============ 카테고리별 게시판 전체 조회 로직.
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllMakerPost(){
        List<MakerPost> makerPostList = makerPostRepository.findAll();
        List<MakerPostGetResponseDto> makerpostGetResponseDtoList = new ArrayList<>();
        for(MakerPost makerPost : makerPostList){
            makerpostGetResponseDtoList.add(
                    MakerPostGetResponseDto.builder()
                            .postId(makerPost.getId())
                            .nickname(makerPost.getMember().getNickname())
                            .position("Maker")
                            .title(makerPost.getTitle())
                            .content(makerPost.getContent())
                            .imageUrl(makerPost.getImageUrl().getImageUrl())
                            .makerlikeCnt(makerLikeRepository.countAllByMakerPost(makerPost))
                            .collaborate(makerPost.getCollaborate())
                            .build()
            );
        }
        return new ResponseEntity<>(Message.success(makerpostGetResponseDtoList),HttpStatus.OK);
    }

    public ResponseEntity<?> getAllSingerPost() {
        List<SingerPost> singerPostList = singerPostRepository.findAll();
        List<SingerPostGetResponseDto> singerpostGetResponseDtoList = new ArrayList<>();
        for (SingerPost singerPost : singerPostList) {
            singerpostGetResponseDtoList.add(
                    SingerPostGetResponseDto.builder()
                            .postId(singerPost.getId())
                            .nickname(singerPost.getMember().getNickname())
                            .position("Singer")
                            .title(singerPost.getTitle())
                            .content(singerPost.getContent())
                            .imageUrl(singerPost.getImageUrl().getImageUrl())
                            .singerlikeCnt(singerLikeRepository.countAllBySingerPost(singerPost))
                            .collaborate(singerPost.getCollaborate())
                            .build()
            );
        }
            return new ResponseEntity<>(Message.success(singerpostGetResponseDtoList), HttpStatus.OK);
        }


    // =============게시판 글 쓰기 로직.
    // 글쓰기 로직 통합.
    @Transactional
    public ResponseEntity<?> createPost(PostCreateRequestDto requestDto, HttpServletRequest request){
        ResponseEntity<?> result = new ResponseEntity<>("",HttpStatus.OK);
        Member member = validation.validateMemberToAccess(request);
        validation.checkAccessToken(request, member);
//        Member memberWhoCreated = validateByNickname(requestDto.getNickname());
        ImageUrl imageUrl = imageUrlSave(requestDto);
        MediaUrl mediaUrl = mediaUrlSave(requestDto);

        if(requestDto.getPosition().equals("Maker")){
            MakerPost createdMakerPost = MakerPost.builder()
                    .likes(0L)
                    .member(member)
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .lyrics(requestDto.getLyrics())
                    .imageUrl(imageUrl)
                    .mediaUrl(mediaUrl)
                    .collaborate(requestDto.getCollaborate())
                    .build();
            makerPostRepository.save(createdMakerPost);
            makerPostTagSave(requestDto.getTags(),createdMakerPost);
            imageUrl.setPostId(createdMakerPost.getId());
            mediaUrl.setPostId(createdMakerPost.getId());
            List<MakerPostTag> makerPostTags= makerPostTagRepository.findAllById(createdMakerPost.getId());
            createdMakerPost.setTags(makerPostTags);

            PostsCreateResponseDto responseDto = PostsCreateResponseDto.builder()
                    .postId(createdMakerPost.getId())
                    .nickname(member.getNickname())
                    .position("Maker")
                    .lyrics(requestDto.getLyrics())
                    .title(createdMakerPost.getTitle())
                    .content(createdMakerPost.getContent())
                    .imageUrl(imageUrl.getImageUrl())
                    .mediaUrl(mediaUrl.getMediaUrl())
                    .tags(requestDto.getTags())
                    .collaborate(createdMakerPost.getCollaborate())
                    .createdAt(createdMakerPost.getCreatedAt())
                    .modifiedAt(createdMakerPost.getModifiedAt())
                    .build();


            result = new ResponseEntity<>(Message.success(responseDto),HttpStatus.OK);
            return result;

        }else if(requestDto.getPosition().equals("Singer")){
            SingerPost createdSingerPost = SingerPost.builder()
                    .likes(0L)
                    .member(member)
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .lyrics(requestDto.getLyrics())
                    .imageUrl(imageUrl)
                    .mediaUrl(mediaUrl)
                    .collaborate(requestDto.getCollaborate())
                    .build();
            singerPostRepository.save(createdSingerPost);
            singerPostTagSave(requestDto.getTags(),createdSingerPost);
            imageUrl.setPostId(createdSingerPost.getId());
            mediaUrl.setPostId(createdSingerPost.getId());
            List<SingerPostTag> singerPostTags= singerPostTagRepository.findAllById(createdSingerPost.getId());
            createdSingerPost.setTags(singerPostTags);

            PostsCreateResponseDto responseDto = PostsCreateResponseDto.builder()
                    .postId(createdSingerPost.getId())
                    .nickname(member.getNickname())
                    .position("Singer")
                    .lyrics(requestDto.getLyrics())
                    .title(createdSingerPost.getTitle())
                    .content(createdSingerPost.getContent())
                    .imageUrl(imageUrl.getImageUrl())
                    .mediaUrl(mediaUrl.getMediaUrl())
                    .tags(requestDto.getTags())
                    .collaborate(createdSingerPost.getCollaborate())
                    .createdAt(createdSingerPost.getCreatedAt())
                    .modifiedAt(createdSingerPost.getModifiedAt())
                    .build();

            result = new ResponseEntity<>(Message.success(responseDto),HttpStatus.OK);

            return result;
        }
        return result;
    }


    //====================게시글 수정 로직
    @Transactional
    public ResponseEntity<?> patchPost(PostPatchRequestDto requestDto,HttpServletRequest request){
        ResponseEntity<?> result = new ResponseEntity<>("",HttpStatus.OK);
        Member member = validation.validateMemberToAccess(request);
        validation.checkAccessToken(request, member);
        String position = requestDto.getPosition();
        Long postId = requestDto.getPostId();

        if(position.equals("Maker")){
            updateUrl(requestDto);
            MakerPost makerPost = makerPostRepository.findById(postId).orElse(new MakerPost());
            makerPost.updateMakerPost(requestDto);
            updateMakerPostTag(makerPost,requestDto);
            result = new ResponseEntity<>(Message.success(
                    PostPatchResponseDto.builder()
                            .postId(postId)
                            .position("Maker")
                            .title(requestDto.getTitle())
                            .content(requestDto.getContent())
                            .nickname(requestDto.getNickname())
                            .lyrics(requestDto.getLyrics())
                            .imageUrl(requestDto.getImageUrl())
                            .mediaUrl(requestDto.getMediaUrl())
                            .tags(requestDto.getTags())
                            .collaborate(requestDto.getCollaborate())
                            .createdAt(makerPost.getCreatedAt())
                            .modifiedAt(makerPost.getModifiedAt())
                            .build()),HttpStatus.OK);
        } else if(position.equals("Singer")){
            updateUrl(requestDto);
            SingerPost singerPost = singerPostRepository.findById(postId).orElse(new SingerPost());
            singerPost.updateSingerPost(requestDto);
            updateSingerPostTag(singerPost,requestDto);
            result = new ResponseEntity<>(Message.success(
                    PostPatchResponseDto.builder()
                            .postId(postId)
                            .position("Singer")
                            .title(requestDto.getTitle())
                            .content(requestDto.getContent())
                            .nickname(requestDto.getNickname())
                            .lyrics(requestDto.getLyrics())
                            .imageUrl(requestDto.getImageUrl())
                            .mediaUrl(requestDto.getMediaUrl())
                            .tags(requestDto.getTags())
                            .collaborate(requestDto.getCollaborate())
                            .createdAt(singerPost.getCreatedAt())
                            .modifiedAt(singerPost.getModifiedAt())
                            .build()),HttpStatus.OK);
        }
        return result;
    }

    //imageUrl 과 mediaUrl 수정.
    @Transactional
    public void updateUrl(PostPatchRequestDto requestDto){
        if(requestDto.getPosition().equals("Maker")){
           MakerPost makerPost = makerPostRepository.findById(requestDto.getPostId()).orElseGet(MakerPost::new);
           ImageUrl imageUrl = makerPost.getImageUrl();
           MediaUrl mediaUrl = makerPost.getMediaUrl();
           imageUrl.updateUrl(requestDto.getImageUrl());
           mediaUrl.updateUrl(requestDto.getMediaUrl());
        }else if(requestDto.getPosition().equals("Singer")){
            SingerPost singerPost = singerPostRepository.findById(requestDto.getPostId()).orElseGet(SingerPost::new);
            ImageUrl imageUrl = singerPost.getImageUrl();
            MediaUrl mediaUrl = singerPost.getMediaUrl();
            imageUrl.updateUrl(requestDto.getImageUrl());
            mediaUrl.updateUrl(requestDto.getMediaUrl());
        }
    }

    //=======================게시판 삭제 로직
    @Transactional
    public  ResponseEntity<?> deletePost(Long postId,String position,HttpServletRequest request) {
        ResponseEntity<?> result = new ResponseEntity<>("", HttpStatus.OK);
        Member member = validation.validateMemberToAccess(request);
        validation.checkAccessToken(request, member);
        if(position.equals("Maker")) {
            MakerPost makerPost = makerPostRepository.findById(postId).orElseGet(MakerPost::new);
            s3Service.delete(makerPost.getImageUrl().getImageUrl());
            makerPostTagRepository.deleteByMakerPostId(makerPost);
            makerLikeRepository.deleteByMakerPostId(makerPost.getId());
            makerPlayListRepository.deleteByMakerPost(makerPost);
            makerPostRepository.delete(makerPost);
            result = new ResponseEntity<>(Message.success("Maker 게시글이 삭제되었습니다"), HttpStatus.OK);
        }
        else if (position.equals("Singer")) {
            SingerPost singerPost = singerPostRepository.findById(postId).orElseGet(SingerPost::new);
            s3Service.delete(singerPost.getImageUrl().getImageUrl());
            singerPostTagRepository.deleteBySingerPostId(singerPost);
            singerLikeRepository.deleteBySingerPostId(singerPost.getId());
            singerPlayListRepository.deleteBySingerPost(singerPost);
            singerPostRepository.delete(singerPost);
            result = new ResponseEntity<>(Message.success("Singer 게시글이 삭제되었습니다."),HttpStatus.OK);
        }
        return result;
    }


//    // Nickname으로 아이디 찾은 Optional 처리 로직.
//    public Member validateByNickname(String nickname){
//        Member member;
//        Optional<Member> memberRepositoryByNickname = memberRepository.findByNickname(nickname);
//        if(memberRepositoryByNickname.isPresent()){
//            member = memberRepositoryByNickname.get();
//        }else{
//            throw new NullPointerException("찾는 닉네임 정보가 없습니다.");
//        }
//        return member;
//    }

    // URL엔티티에 저장 로직
    public ImageUrl imageUrlSave(PostCreateRequestDto requestDto){
        ImageUrl imageUrl =  ImageUrl.builder()
                .postId(null)
                .position(requestDto.getPosition())
                .imageUrl(requestDto.getImageUrl())
                .build();

        imageUrlRepository.save(imageUrl);
        return imageUrl;
    }

    public MediaUrl mediaUrlSave(PostCreateRequestDto requestDto){
        MediaUrl mediaUrl = MediaUrl.builder()
                .postId(null)
                .position(requestDto.getPosition())
                .mediaUrl(requestDto.getMediaUrl())
                .build();
        mediaUrlRepository.save(mediaUrl);
        return mediaUrl;
    }

    public List<Tag> stringListSaveToTag(List<String> tags){
        List<Tag> tagIds = new ArrayList<>();
        for (String s : tags) {
            Tag tag = new Tag();
            tag.setTag(s);
            tagRepository.save(tag);
            tagIds.add(tag);
        }
        return tagIds;
    }
    public void makerPostTagSave(List<String> tags,MakerPost makerPost){
        for(String tag : tags){
            Tag tag1 = tagRepository.save(
                    Tag.builder()
                            .tag(tag)
                            .build());
            MakerPostTag makerPostTag = new MakerPostTag(makerPost,tag1);
            makerPostTagRepository.save(makerPostTag);
        }
    }

    public void singerPostTagSave(List<String> tags,SingerPost singerPost){
        for(String tag : tags){
            Tag tag1 = tagRepository.save(
                    Tag.builder()
                            .tag(tag)
                            .build());
            SingerPostTag singerPostTag = new SingerPostTag(singerPost,tag1);
            singerPostTagRepository.save(singerPostTag);
        }

    }


    @Transactional
    public void updateMakerPostTag(MakerPost makerPost, PostPatchRequestDto requestDto) {
        makerPostTagRepository.deleteByMakerPostId(makerPost);
        makerPostTagSave(requestDto.getTags(),makerPost);
    }

    @Transactional
    public void updateSingerPostTag(SingerPost singerPost, PostPatchRequestDto requestDto) {
        singerPostTagRepository.deleteBySingerPostId(singerPost);
        singerPostTagSave(requestDto.getTags(), singerPost);
    }
    //Position 으로 FK 값 찾기
    public MakerPost findMakerPostByPostId(Long postId){
        return makerPostRepository.findById(postId).orElseGet(MakerPost::new);
    }
    public SingerPost findSingerPostByPostId(Long postId){
        return singerPostRepository.findById(postId).orElseGet(SingerPost::new);
    }

    public void imageUrlDelete(){
//        imageUrlRepository.deleteByPostId();
    }

    public void mediaUrlDelete(){

    }
}
