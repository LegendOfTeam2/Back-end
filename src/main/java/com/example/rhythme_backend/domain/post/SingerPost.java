package com.example.rhythme_backend.domain.post;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.media.ImageUrl;
import com.example.rhythme_backend.domain.media.MediaUrl;
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
public class SingerPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "imageUrl")
    @ManyToOne(fetch = FetchType.LAZY)
    private ImageUrl imageUrl;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private MediaUrl mediaUrl;

    @Column(name="tags")
    @ElementCollection(targetClass=String.class)
    private List<String> tags;

}
