package com.example.rhythme_backend.domain.post;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.Tag;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.util.Timestamped;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SingerPost extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,length = 1500)
    private String content;

    @Column(nullable = false,length = 1500)
    private String lyrics;

    @JoinColumn(name = "image_url")
    @OneToOne(fetch = FetchType.EAGER)
    private ImageUrl imageUrl;

    @JoinColumn(name = "media_url")
    @OneToOne(fetch = FetchType.EAGER)
    private MediaUrl mediaUrl;

    @OneToMany(mappedBy = "tagId",fetch = FetchType.LAZY)
    private List<SingerPostTag> tags;

    @Column
    private Long likes;

    @Column(nullable = false)
    private Boolean collaborate;

    public void updateSingerPost(PostPatchRequestDto patchRequestDto){
        this.lyrics = patchRequestDto.getLyrics();
        this.content = patchRequestDto.getContent();
        this.title = patchRequestDto.getTitle();
        this.collaborate = patchRequestDto.getCollaborate();
    }

    public void singerUpdateLikes(Long likes) {
        this.likes = likes;
    }

}
