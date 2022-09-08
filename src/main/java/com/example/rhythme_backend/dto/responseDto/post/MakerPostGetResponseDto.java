package com.example.rhythme_backend.dto.responseDto.post;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MakerPostGetResponseDto {
    private Long postId;
    private String nickname;

    private String position;
    private String title;
    private String content;
    private String imageUrl;
    private String mediaUrl;
//    private List<Tag> tags;
    private Long makerlikeCnt;
    private Boolean collaborate;

    public MakerPostGetResponseDto(Long postId, String nickname, String position, String title, String content, String imageUrl,
                                   String mediaUrl ,Long makerlikeCnt , Boolean collaborate){
        this.postId=postId;
        this.nickname=nickname;
        this.position=position;
        this.title=title;
        this.content=content;
        this.imageUrl=imageUrl;
        this.mediaUrl=mediaUrl;
//        this.tags=tags;
        this.makerlikeCnt=makerlikeCnt;
        this.collaborate=collaborate;

    }
}