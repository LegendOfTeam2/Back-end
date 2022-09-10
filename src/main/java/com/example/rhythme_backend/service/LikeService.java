package com.example.rhythme_backend.service;

import com.example.rhythme_backend.domain.like.MakerLike;
import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.like.SingerLike;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.dto.requestDto.like.MakerLikeRequestDto;
import com.example.rhythme_backend.dto.requestDto.like.SingerLikeRequestDto;
import com.example.rhythme_backend.exception.CustomException;
import com.example.rhythme_backend.exception.ErrorCode;
import com.example.rhythme_backend.jwt.TokenProvider;
import com.example.rhythme_backend.repository.like.MakerLikeRepository;
import com.example.rhythme_backend.repository.like.SingerLikeRepository;
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
    public ResponseDto<?> upDownSingerLike(Long postId, HttpServletRequest request) {
        Member member = validateMember(request);
        checkAccessToken(request, member);
        SingerPost singerPost = getCurrentSingerPost(postId);
        checkSingerPost(singerPost);

        SingerLike findSingerLike = singerLikeRepository.findByMemberAndSingerPost(member,singerPost).orElse(null);
        if(findSingerLike == null){
            SingerLikeRequestDto singerLikeRequestDto = new SingerLikeRequestDto(member, singerPost);
            SingerLike singerLike = new SingerLike(singerLikeRequestDto);
            singerLikeRepository.save(singerLike);
            Long likes = singerLikeRepository.countAllBySingerPostId(postId);
            singerPost.singerUpdateLikes(likes);
            return ResponseDto.success(true);
        } else {
            singerLikeRepository.deleteById(findSingerLike.getId());
            Long likes = singerLikeRepository.countAllBySingerPostId(postId);
            singerPost.singerUpdateLikes(likes);
            return ResponseDto.success(false);
        }
    }

    @Transactional
    public ResponseDto<?> upDownMakerLike(Long postId, HttpServletRequest request) {
        Member member = validateMember(request);
        checkAccessToken(request, member);
        MakerPost makerPost = getCurrentMakerPost(postId);
        checkMakerPost(makerPost);

        MakerLike findMakerLike = makerLikeRepository.findByMemberIdAndMakerPost(member,makerPost).orElse(null);

        if(findMakerLike == null){
            MakerLikeRequestDto makerLikeRequestDto = new MakerLikeRequestDto(member, makerPost);
            MakerLike makerLike = new MakerLike(makerLikeRequestDto);
            makerLikeRepository.save(makerLike);
            Long likes = makerLikeRepository.countAllByMakerPostId(postId);
            makerPost.makerUpdateLikes(likes);
            return ResponseDto.success(true);
        } else {
            makerLikeRepository.deleteById(findMakerLike.getId());
            Long likes = makerLikeRepository.countAllByMakerPostId(postId);
            makerPost.makerUpdateLikes(likes);
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
