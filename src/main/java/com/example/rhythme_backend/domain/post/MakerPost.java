package com.example.rhythme_backend.domain.post;


import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
import com.example.rhythme_backend.dto.requestDto.post.PostPatchRequestDto;
import com.example.rhythme_backend.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
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

    @JoinColumn(name = "imageUrl")
    @ManyToOne(fetch = FetchType.EAGER)
    private ImageUrl imageUrl;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private MediaUrl mediaUrl;

    @Column(name="tag")
    @ElementCollection(targetClass=String.class)
    private List<String> tag;

    public void updateMakerPost(PostPatchRequestDto patchRequestDto){
        this.content = patchRequestDto.getContent();
        this.title = patchRequestDto.getTitle();
        this.tag = patchRequestDto.getTag();
    }
}
