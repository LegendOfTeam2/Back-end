package com.example.rhythme_backend.service;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.requestDto.PostRequestDto;
import com.example.rhythme_backend.repository.*;
import com.example.rhythme_backend.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final SingerPostRepository singerPostRepository;
    private final MakerPostRepository makerPostRepository;

    private final ImageUrlRepository imageUrlRepository;
    private final MediaUrlRepository mediaUrlRepository;


    //============ 카테고리별 게시판 전체 조회 로직.
    public ResponseEntity<?> getAllMakerPost(){
        List<MakerPost> makerPostList = makerPostRepository.findAll();
        return new ResponseEntity<>(Message.success(makerPostList),HttpStatus.OK);
    }

    public ResponseEntity<?> getAllSingerPost(){
        List<SingerPost> singerPostList = singerPostRepository.findAll();
        return new ResponseEntity<>(Message.success(singerPostList), HttpStatus.OK);
    }
    // =============



    // =============카테고리별 게시판 글 쓰기 로직.
    // 메이커 글쓰기
    public ResponseEntity<?> createMakerPost(PostRequestDto postRequestDto){

        //validation 처리할게 뭐 있는지 확인.

        Member memberWhoCreated = validateByEmail(postRequestDto);
        ImageUrl imageUrl = imageUrlSave(postRequestDto,memberWhoCreated);
        MediaUrl mediaUrl = mediaUrlSave(postRequestDto,memberWhoCreated);
            MakerPost createdMakerPost = MakerPost.builder()
                    .member(memberWhoCreated)
                    .title(postRequestDto.getTitle())
                    .content(postRequestDto.getContent())
                    .imageUrl(imageUrl)
                    .mediaUrl(mediaUrl)
                    .build();

            makerPostRepository.save(createdMakerPost);


        //postRequestDto 받은걸로 생성 로직
        return new ResponseEntity<>(Message.success(createdMakerPost),HttpStatus.OK);
    }

    // 싱어 글쓰기
    public ResponseEntity<?> createSingerPost(PostRequestDto postRequestDto){

        //validation 처리할게 뭐 있는지 확인.
        Member memberWhoCreated = validateByEmail(postRequestDto);
        ImageUrl imageUrl = imageUrlSave(postRequestDto,memberWhoCreated);
        MediaUrl mediaUrl = mediaUrlSave(postRequestDto,memberWhoCreated);
            SingerPost createdSingerPost = SingerPost.builder()
                    .member(memberWhoCreated)
                    .title(postRequestDto.getTitle())
                    .content(postRequestDto.getContent())
                    .imageUrl(imageUrl)
                    .mediaUrl(mediaUrl)
                    .build();

            singerPostRepository.save(createdSingerPost);


        //postRequestDto 받은걸로 생성 로직
        return new ResponseEntity<>(Message.success(createdSingerPost),HttpStatus.OK);
    }

    //====================



    // EMAIL로 아이디 찾은 Optional 처리 로직.
    public Member validateByEmail(PostRequestDto postRequestDto){
        Member member;
        Optional<Member> memberRepositoryByEmail = memberRepository.findByEmail(postRequestDto.getEmail());
        if(memberRepositoryByEmail.isPresent()){
            member = memberRepositoryByEmail.get();
        }else{
            throw new NullPointerException("찾는 이메일 정보가 없습니다.");
        }
        return member;
    }

    // 이미지 엔티티에 저장 로직
    public ImageUrl imageUrlSave(PostRequestDto postRequestDto, Member member){
        ImageUrl imageUrl =  ImageUrl.builder()
                            .member(member)
                            .ImageUrl(postRequestDto.getImageUrl())
                            .build();

        imageUrlRepository.save(imageUrl);
        return imageUrl;
    }


    // 미디어 엔티티에 저장 로직
    public MediaUrl mediaUrlSave(PostRequestDto postRequestDto, Member member){
        MediaUrl mediaUrl = MediaUrl.builder()
                            .member(member)
                            .MediaUrl(postRequestDto.getMediaUrl())
                            .build();
        mediaUrlRepository.save(mediaUrl);
        return mediaUrl;
    }

}
