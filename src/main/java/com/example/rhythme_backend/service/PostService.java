package com.example.rhythme_backend.service;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.Tag;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.MakerPostTag;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.domain.post.SingerPostTag;
import com.example.rhythme_backend.dto.requestDto.post.PostCreateRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostDeleteRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.dto.responseDto.post.*;
import com.example.rhythme_backend.repository.MemberRepository;
import com.example.rhythme_backend.repository.TagRepository;
import com.example.rhythme_backend.repository.like.MakerLikeRepository;
import com.example.rhythme_backend.repository.like.SingerLikeRepository;
import com.example.rhythme_backend.repository.media.ImageUrlRepository;
import com.example.rhythme_backend.repository.media.MediaUrlRepository;
import com.example.rhythme_backend.repository.posts.MakerPostRepository;
import com.example.rhythme_backend.repository.posts.MakerPostTagRepository;
import com.example.rhythme_backend.repository.posts.SingerPostRepository;
import com.example.rhythme_backend.repository.posts.SingerPostTagRepository;
import com.example.rhythme_backend.util.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService{
    private final MemberRepository memberRepository;
    private final SingerPostRepository singerPostRepository;
    private final MakerPostRepository makerPostRepository;

    // 게시글에 달려있는 이미지,태그
    private final TagRepository tagRepository;
    private final ImageUrlRepository imageUrlRepository;
    private final MediaUrlRepository mediaUrlRepository;

    private final MakerPostTagRepository makerPostTagRepository;

    private final SingerPostTagRepository singerPostTagRepository;
    private final MakerLikeRepository makerLikeRepository;
    //AWS S3
    private final S3Service s3Service;
    private final SingerLikeRepository singerLikeRepository;

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
    public ResponseEntity<?> createPost(PostCreateRequestDto postCreateRequestDto){
        ResponseEntity<?> result = new ResponseEntity<>("",HttpStatus.OK);

        Member memberWhoCreated = validateByNickname(postCreateRequestDto.getNickname());
        ImageUrl imageUrl = imageUrlSave(postCreateRequestDto);
        MediaUrl mediaUrl = mediaUrlSave(postCreateRequestDto);

        if(postCreateRequestDto.getPosition().equals("Maker")){
            MakerPost createdMakerPost = MakerPost.builder()
                    .likes(0L)
                    .member(memberWhoCreated)
                    .title(postCreateRequestDto.getTitle())
                    .content(postCreateRequestDto.getContent())
                    .lyrics(postCreateRequestDto.getLyrics())
                    .imageUrl(imageUrl)
                    .mediaUrl(mediaUrl)
                    .collaborate(postCreateRequestDto.getCollaborate())
                    .build();
            makerPostRepository.save(createdMakerPost);
            makerPostTagSave(postCreateRequestDto.getTags(),createdMakerPost);
            imageUrl.setPostId(createdMakerPost.getId());
            mediaUrl.setPostId(createdMakerPost.getId());
            List<MakerPostTag> makerPostTags= makerPostTagRepository.findAllById(createdMakerPost.getId());
            createdMakerPost.setTags(makerPostTags);

            PostsCreateResponseDto responseDto = PostsCreateResponseDto.builder()
                    .postId(createdMakerPost.getId())
                    .nickname(memberWhoCreated.getNickname())
                    .position("Maker")
                    .lyrics(postCreateRequestDto.getLyrics())
                    .title(createdMakerPost.getTitle())
                    .content(createdMakerPost.getContent())
                    .imageUrl(imageUrl.getImageUrl())
                    .mediaUrl(mediaUrl.getMediaUrl())
                    .tags(postCreateRequestDto.getTags())
                    .collaborate(createdMakerPost.getCollaborate())
                    .createdAt(createdMakerPost.getCreatedAt())
                    .modifiedAt(createdMakerPost.getModifiedAt())
                    .build();


            result = new ResponseEntity<>(Message.success(responseDto),HttpStatus.OK);

            return result;

        }else if(postCreateRequestDto.getPosition().equals("Singer")){
            SingerPost createdSingerPost = SingerPost.builder()
                    .likes(0L)
                    .member(memberWhoCreated)
                    .title(postCreateRequestDto.getTitle())
                    .content(postCreateRequestDto.getContent())
                    .lyrics(postCreateRequestDto.getLyrics())
                    .imageUrl(imageUrl)
                    .mediaUrl(mediaUrl)
                    .collaborate(postCreateRequestDto.getCollaborate())
                    .build();
            singerPostRepository.save(createdSingerPost);
            singerPostTagSave(postCreateRequestDto.getTags(),createdSingerPost);
            imageUrl.setPostId(createdSingerPost.getId());
            mediaUrl.setPostId(createdSingerPost.getId());
            List<SingerPostTag> singerPostTags= singerPostTagRepository.findAllById(createdSingerPost.getId());
            createdSingerPost.setTags(singerPostTags);

            PostsCreateResponseDto responseDto = PostsCreateResponseDto.builder()
                    .postId(createdSingerPost.getId())
                    .nickname(memberWhoCreated.getNickname())
                    .position("Singer")
                    .lyrics(postCreateRequestDto.getLyrics())
                    .title(createdSingerPost.getTitle())
                    .content(createdSingerPost.getContent())
                    .imageUrl(imageUrl.getImageUrl())
                    .mediaUrl(mediaUrl.getMediaUrl())
                    .tags(postCreateRequestDto.getTags())
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
    public ResponseEntity<?> patchPost(PostPatchRequestDto postPatchRequestDto){
        ResponseEntity<?> result = new ResponseEntity<>("",HttpStatus.OK);
        String position = postPatchRequestDto.getPosition();
        Long postId = postPatchRequestDto.getPostId();

        if(position.equals("Maker")){
            updateUrl(postPatchRequestDto);
            MakerPost makerPost = makerPostRepository.findById(postId).orElse(new MakerPost());
            makerPost.updateMakerPost(postPatchRequestDto);
            updateMakerPostTag(makerPost,postPatchRequestDto);
            result = new ResponseEntity<>(Message.success(
                    PostPatchResponseDto.builder()
                            .postId(postId)
                            .position("Maker")
                            .title(postPatchRequestDto.getTitle())
                            .content(postPatchRequestDto.getContent())
                            .nickname(postPatchRequestDto.getNickname())
                            .lyrics(postPatchRequestDto.getLyrics())
                            .imageUrl(postPatchRequestDto.getImageUrl())
                            .mediaUrl(postPatchRequestDto.getMediaUrl())
                            .tags(postPatchRequestDto.getTags())
                            .collaborate(postPatchRequestDto.getCollaborate())
                            .createdAt(makerPost.getCreatedAt())
                            .modifiedAt(makerPost.getModifiedAt())
                            .build()),HttpStatus.OK);
        } else if(position.equals("Singer")){
            updateUrl(postPatchRequestDto);
           SingerPost singerPost = singerPostRepository.findById(postId).orElse(new SingerPost());
           singerPost.updateSingerPost(postPatchRequestDto);
           updateSingerPostTag(singerPost,postPatchRequestDto);
           result = new ResponseEntity<>(Message.success(
                   PostPatchResponseDto.builder()
                           .postId(postId)
                           .position("Singer")
                           .title(postPatchRequestDto.getTitle())
                           .content(postPatchRequestDto.getContent())
                           .nickname(postPatchRequestDto.getNickname())
                           .lyrics(postPatchRequestDto.getLyrics())
                           .imageUrl(postPatchRequestDto.getImageUrl())
                           .mediaUrl(postPatchRequestDto.getMediaUrl())
                           .tags(postPatchRequestDto.getTags())
                           .collaborate(postPatchRequestDto.getCollaborate())
                           .createdAt(singerPost.getCreatedAt())
                           .modifiedAt(singerPost.getModifiedAt())
                           .build()),HttpStatus.OK);
        }
        return result;
    }

    //imageUrl 과 mediaUrl 수정.
    public void updateUrl(PostPatchRequestDto postPatchRequestDto){
        if(postPatchRequestDto.getPosition().equals("Maker")){
           MakerPost makerPost = findMakerPostByPostId(postPatchRequestDto.getPostId());
           ImageUrl imageUrl = makerPost.getImageUrl();
           MediaUrl mediaUrl = makerPost.getMediaUrl();
           imageUrl.updateUrl(postPatchRequestDto.getImageUrl());
           mediaUrl.updateUrl(postPatchRequestDto.getMediaUrl());
        }else if(postPatchRequestDto.getPosition().equals("Signer")){
            SingerPost singerPost = findSingerPostByPostId(postPatchRequestDto.getPostId());
            ImageUrl imageUrl = singerPost.getImageUrl();
            MediaUrl mediaUrl = singerPost.getMediaUrl();
            imageUrl.updateUrl(postPatchRequestDto.getImageUrl());
            mediaUrl.updateUrl(postPatchRequestDto.getMediaUrl());
        }
    }




    //=======================게시판 삭제 로직
    @Transactional
    public  ResponseEntity<?> deletePost(PostDeleteRequestDto postDeleteRequestDto) {
        ResponseEntity<?> result = new ResponseEntity<>("", HttpStatus.OK);

        String position = postDeleteRequestDto.getPosition();
        Long postId = postDeleteRequestDto.getPostId();

        if(position.equals("Maker")) {
            MakerPost makerPost = makerPostRepository.findById(postId).orElseGet(MakerPost::new);
            s3Service.delete(makerPost.getImageUrl().getImageUrl());
            List<MakerPostTag> makerPostTags = makerPostTagRepository.findAllByMakerPostId(makerPost);
            makerPostTagRepository.deleteByMakerPostId(makerPost);
            makerPostRepository.delete(makerPost);
            for (int i = 0; i < makerPostTags.size(); i++) {
                tagRepository.deleteById(makerPostTags.get(i).getTagId().getId());
            }
            result = new ResponseEntity<>(Message.success("Maker 게시글이 삭제되었습니다"), HttpStatus.OK);
        }
        else if (position.equals("Singer")) {
            SingerPost singerPost = singerPostRepository.findById(postId).orElseGet(SingerPost::new);
            s3Service.delete(singerPost.getImageUrl().getImageUrl());
            List<SingerPostTag> singerPostTags = singerPostTagRepository.findAllBySingerPostId(singerPost);
            singerPostTagRepository.deleteBySingerPostId(singerPost);
            singerPostRepository.delete(singerPost);
            for (int i = 0; i < singerPostTags.size(); i++) {
                tagRepository.deleteById(singerPostTags.get(i).getTagId().getId());
            }
            result = new ResponseEntity<>(Message.success("Singer 게시글이 삭제되었습니다."),HttpStatus.OK);
        }
        return result;
    }


    //

    // Nickname으로 아이디 찾은 Optional 처리 로직.
    public Member validateByNickname(String nickname){
        Member member;
        Optional<Member> memberRepositoryByNickname = memberRepository.findByNickname(nickname);
        if(memberRepositoryByNickname.isPresent()){
            member = memberRepositoryByNickname.get();
        }else{
            throw new NullPointerException("찾는 닉네임 정보가 없습니다.");
        }
        return member;
    }
    //Position 으로 FK 값 찾기
    public MakerPost findMakerPostByPostId(Long postId){
        return makerPostRepository.findById(postId).orElseGet(MakerPost::new);
    }
    public SingerPost findSingerPostByPostId(Long postId){
        return singerPostRepository.findById(postId).orElseGet(SingerPost::new);
    }

    // URL엔티티에 저장 로직
    public ImageUrl imageUrlSave(PostCreateRequestDto postCreateRequestDto){
        ImageUrl imageUrl =  ImageUrl.builder()
                .postId(null)
                .position(postCreateRequestDto.getPosition())
                .imageUrl(postCreateRequestDto.getImageUrl())
                .build();

        imageUrlRepository.save(imageUrl);
        return imageUrl;
    }

    public MediaUrl mediaUrlSave(PostCreateRequestDto postCreateRequestDto){
        MediaUrl mediaUrl = MediaUrl.builder()
                .postId(null)
                .position(postCreateRequestDto.getPosition())
                .mediaUrl(postCreateRequestDto.getMediaUrl())
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


    public void updateMakerPostTag(MakerPost makerPost, PostPatchRequestDto patchRequestDto) {
        List<MakerPostTag> makerPostTags = makerPostTagRepository.findAllByMakerPostId(makerPost);
        for (int i = 0; i < makerPostTags.size(); i++) {
            tagRepository.deleteById(makerPostTags.get(i).getTagId().getId());
        }
        makerPostTagRepository.deleteByMakerPostId(makerPost);
        makerPostTagSave(patchRequestDto.getTags(), makerPost);
    }

    public void updateSingerPostTag(SingerPost singerPost, PostPatchRequestDto patchRequestDto) {
        List<SingerPostTag> singerPostTags = singerPostTagRepository.findAllBySingerPostId(singerPost);
        for(int i =0; i< singerPostTags.size(); i++){
            tagRepository.deleteById(singerPostTags.get(i).getTagId().getId());
        }
        singerPostTagRepository.deleteBySingerPostId(singerPost);
        singerPostTagSave(patchRequestDto.getTags(), singerPost);
    }


    public void imageUrlDelete(){
//        imageUrlRepository.deleteByPostId();
    }


    public void mediaUrlDelete(){

    }
}
