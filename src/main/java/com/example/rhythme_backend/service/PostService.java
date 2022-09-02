package com.example.rhythme_backend.service;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.requestDto.post.PostCreateRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostDeleteRequestDto;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.dto.responseDto.post.PostCreateResponseDto;
import com.example.rhythme_backend.repository.*;
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
public class PostService<T>{
    private final MemberRepository memberRepository;
    private final SingerPostRepository singerPostRepository;
    private final MakerPostRepository makerPostRepository;

    private final ImageUrlRepository imageUrlRepository;
    private final MediaUrlRepository mediaUrlRepository;

    private T positionValue;


    //============ 카테고리별 게시판 전체 조회 로직.
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAllMakerPost(){
        List<MakerPost> makerPostList = makerPostRepository.findAll();
        List<PostCreateResponseDto> postCreateResponseDtoList = new ArrayList<>();
        for(MakerPost makerPost : makerPostList){
            postCreateResponseDtoList.add(
                    PostCreateResponseDto.builder()
                            .postId(makerPost.getId())
                            .email(makerPost.getMember().getEmail())
                            .position("Maker")
                            .title(makerPost.getTitle())
                            .content(makerPost.getContent())
                            .imageUrl(makerPost.getImageUrl().getImageUrl())
                            .tag(makerPost.getTag())
                            .build()
            );
        }
        return new ResponseEntity<>(Message.success(postCreateResponseDtoList),HttpStatus.OK);
    }

    public ResponseEntity<?> getAllSingerPost(){
        List<SingerPost> singerPostList = singerPostRepository.findAll();
        List<PostCreateResponseDto> postCreateResponseDtoList = new ArrayList<>();
        for(SingerPost singerPost : singerPostList){
            postCreateResponseDtoList.add(
                    PostCreateResponseDto.builder()
                            .postId(singerPost.getId())
                            .email(singerPost.getMember().getEmail())
                            .position("Singer")
                            .title(singerPost.getTitle())
                            .content(singerPost.getContent())
                            .imageUrl(singerPost.getImageUrl().getImageUrl())
                            .tag(singerPost.getTag())
                            .build()
            );
        }
        return new ResponseEntity<>(Message.success(postCreateResponseDtoList),HttpStatus.OK);
    }



    // =============게시판 글 쓰기 로직.
    // 글쓰기 로직 통합.
    @Transactional
    public ResponseEntity<?> createPost(PostCreateRequestDto postCreateRequestDto){
        ResponseEntity<?> result = new ResponseEntity<>("",HttpStatus.OK);

        Member memberWhoCreated = validateByEmail(postCreateRequestDto.getEmail());
        ImageUrl imageUrl = imageUrlSave(postCreateRequestDto);
        MediaUrl mediaUrl = mediaUrlSave(postCreateRequestDto);

        if(postCreateRequestDto.getPosition().equals("Singer")){
            MakerPost createdMakerPost = MakerPost.builder()
                    .member(memberWhoCreated)
                    .title(postCreateRequestDto.getTitle())
                    .content(postCreateRequestDto.getContent())
                    .imageUrl(imageUrl)
                    .mediaUrl(mediaUrl)
                    .tag(postCreateRequestDto.getTag())
                    .build();
            makerPostRepository.save(createdMakerPost);
            result = new ResponseEntity<>(Message.success(createdMakerPost),HttpStatus.OK);

            return result;

        }else if(postCreateRequestDto.getPosition().equals("Maker")){
            SingerPost createdSingerPost = SingerPost.builder()
                    .member(memberWhoCreated)
                    .title(postCreateRequestDto.getTitle())
                    .content(postCreateRequestDto.getContent())
                    .imageUrl(imageUrl)
                    .mediaUrl(mediaUrl)
                    .tag(postCreateRequestDto.getTag())
                    .build();
            singerPostRepository.save(createdSingerPost);
            result = new ResponseEntity<>(Message.success(createdSingerPost),HttpStatus.OK);

            return result;
        }

        return result;
    }




    //====================게시글 수정 로직
    @Transactional
    public ResponseEntity<?> patchPost(PostPatchRequestDto postPutRequestDto){
        ResponseEntity<?> result = new ResponseEntity<>("",HttpStatus.OK);

        String position = postPutRequestDto.getPosition();
        Long postId = postPutRequestDto.getPostId();

        if(position.equals("Singer")){
            updateUrl(postPutRequestDto);
           SingerPost singerPost = singerPostRepository.findById(postId).orElse(new SingerPost());
           singerPost.updateSingerPost(postPutRequestDto);
           result = new ResponseEntity<>(Message.success(singerPost),HttpStatus.OK);
        }else if(position.equals("Maker")){
            updateUrl(postPutRequestDto);
            MakerPost makerPost = makerPostRepository.findById(postId).orElse(new MakerPost());
            makerPost.updateMakerPost(postPutRequestDto);
            result = new ResponseEntity<>(Message.success(makerPost),HttpStatus.OK);
        }
        return result;
    }

    //imageUrl 과 mediaUrl 수정 .
    public void updateUrl(PostPatchRequestDto postPatchRequestDto){
        if(postPatchRequestDto.getPosition().equals("Maker")){
           MakerPost makerPost = findMakerPostByPostId(postPatchRequestDto.getPosition(), postPatchRequestDto.getPostId());
           ImageUrl imageUrl = makerPost.getImageUrl();
           MediaUrl mediaUrl = makerPost.getMediaUrl();
           imageUrl.updateUrl(postPatchRequestDto.getImageUrl());
           mediaUrl.updateUrl(postPatchRequestDto.getMediaUrl());
        }else if(postPatchRequestDto.getPosition().equals("Signer")){
            SingerPost singerPost = findSingerPostByPostId(postPatchRequestDto.getPosition(), postPatchRequestDto.getPostId());
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

        if (position.equals("Singer")) {
            SingerPost singerPost = singerPostRepository.findById(postId).orElseGet(SingerPost::new);
            singerPostRepository.delete(singerPost);
            result = new ResponseEntity<>(Message.success("Singer 게시글이 삭제되었습니다."),HttpStatus.OK);
        } else if(position.equals("Maker")) {
            MakerPost makerPost = makerPostRepository.findById(postId).orElseGet(MakerPost::new);
            makerPostRepository.delete(makerPost);
            result = new ResponseEntity<>(Message.success("Maker 게시글이 삭제되었습니다"), HttpStatus.OK);
        }
        return result;
    }


    //

    // EMAIL로 아이디 찾은 Optional 처리 로직.
    public Member validateByEmail(String email){
        Member member;
        Optional<Member> memberRepositoryByEmail = memberRepository.findByEmail(email);
        if(memberRepositoryByEmail.isPresent()){
            member = memberRepositoryByEmail.get();
        }else{
            throw new NullPointerException("찾는 이메일 정보가 없습니다.");
        }
        return member;
    }
    //Position 으로 FK 값 찾기
    public MakerPost findMakerPostByPostId(String position,Long postId){
        MakerPost makerPost = makerPostRepository.findById(postId).orElseGet(MakerPost::new);
        return makerPost;
    }
    public SingerPost findSingerPostByPostId(String position,Long postId){
        SingerPost singerPost = singerPostRepository.findById(postId).orElseGet(SingerPost::new);
        return singerPost;
    }

    // 이미지 엔티티에 저장 로직
    public ImageUrl imageUrlSave(PostCreateRequestDto postCreateRequestDto){
        ImageUrl imageUrl =  ImageUrl.builder()
                            .imageUrl(postCreateRequestDto.getImageUrl())
                            .build();

        imageUrlRepository.save(imageUrl);
        return imageUrl;
    }


    // 미디어 엔티티에 저장 로직
    public MediaUrl mediaUrlSave(PostCreateRequestDto postCreateRequestDto){
        MediaUrl mediaUrl = MediaUrl.builder()
                            .mediaUrl(postCreateRequestDto.getMediaUrl())
                            .build();
        mediaUrlRepository.save(mediaUrl);
        return mediaUrl;
    }

}
