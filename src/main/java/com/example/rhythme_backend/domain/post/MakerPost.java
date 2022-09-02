package com.example.rhythme_backend.domain.post;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.Tag;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class MakerPost extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "member", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;


    @JoinColumn(name = "image_url")
    @OneToOne(fetch = FetchType.EAGER)
    private ImageUrl imageUrl;


    @JoinColumn(name = "media_url")
    @OneToOne(fetch = FetchType.EAGER)
    private MediaUrl mediaUrl;


    @OneToMany(mappedBy ="maker_post", fetch = FetchType.LAZY)
    private List<Tag> tags;


    public void updateMakerPost(PostPatchRequestDto patchRequestDto){
        this.content = patchRequestDto.getContent();
        this.title = patchRequestDto.getTitle();
    }
}