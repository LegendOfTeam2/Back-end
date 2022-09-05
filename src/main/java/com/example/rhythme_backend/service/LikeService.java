package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.MakerLike;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.SingerLike;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.requestDto.MakerLikeRequestDto;
import com.example.rhythme_backend.dto.requestDto.SingerLikeRequestDto;
import com.example.rhythme_backend.exception.CustomException;
import com.example.rhythme_backend.exception.ErrorCode;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.MakerLikeRepository;
import com.example.rhythme_backend.repository.SingerLikeRepository;
import com.example.rhythme_backend.repository.posts.MakerPostRepository;
import com.example.rhythme_backend.repository.posts.SingerPostRepository;
import com.example.rhythme_backend.util.ResponseDto;
import com.example.rhythme_backend.util.exception.NotFoundPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final TokenProvider tokenProvider;

    private final SingerPostRepository singerPostRepository;

    private final MakerPostRepository makerPostRepository;

    private final SingerLikeRepository singerLikeRepository;

    private final MakerLikeRepository makerLikeRepository;

    @Transactional
    public ResponseDto<?> upDownSingerLike(Long singerPostId, HttpServletRequest request) {
        Member member = validateMember(request);
        checkAccessToken(request, member);
        SingerPost singerPost = getCurrentSingerPost(singerPostId);
        checkSingerPost(singerPost);

        SingerLike findSingerLike = singerLikeRepository.findByMemberAndSingerPost(member,singerPost).orElse(null);
        if(findSingerLike == null){
            SingerLikeRequestDto singerLikeRequestDto = new SingerLikeRequestDto(member, singerPost);
            SingerLike singerLike = new SingerLike(singerLikeRequestDto);
            singerLikeRepository.save(singerLike);
            return ResponseDto.success(true);
        } else {
            singerLikeRepository.deleteById(findSingerLike.getId());
            return ResponseDto.success(false);
        }
    }

    @Transactional
    public ResponseDto<?> upDownMakerLike(Long makerPostId, HttpServletRequest request) {
        Member member = validateMember(request);
        checkAccessToken(request, member);
        MakerPost makerPost = getCurrentMakerPost(makerPostId);
        checkMakerPost(makerPost);

        MakerLike findMakerLike = makerLikeRepository.findByMemberAndMakerPost(member,makerPost).orElse(null);

        if(findMakerLike == null){
            MakerLikeRequestDto makerLikeRequestDto = new MakerLikeRequestDto(member, makerPost);
            MakerLike makerLike = new MakerLike(makerLikeRequestDto);
            makerLikeRepository.save(makerLike);
            return ResponseDto.success(true);
        } else {
            singerLikeRepository.deleteById(findMakerLike.getId());
            return ResponseDto.success(false);
        }
    }

    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public void checkAccessToken (HttpServletRequest request, Member member){
        if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7)))
            throw new CustomException(ErrorCode.TOKEN_IS_EXPIRED);
        if (null == member) throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public SingerPost getCurrentSingerPost(Long id) {
        Optional<SingerPost> optionalSingerPost = singerPostRepository.findById(id);
        return optionalSingerPost.orElse(null);
    }

    @Transactional(readOnly = true)
    public MakerPost getCurrentMakerPost(Long id) {
        Optional<MakerPost> optionalMakerPost = makerPostRepository.findById(id);
        return optionalMakerPost.orElse(null);
    }



    public void checkSingerPost(SingerPost singerPost) {
        if (singerPost == null) throw new NotFoundPostException();
    }

    public void checkMakerPost(MakerPost makerPost) {
        if (makerPost == null) throw new NotFoundPostException();
    }

}