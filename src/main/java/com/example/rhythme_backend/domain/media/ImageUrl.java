package com.example.rhythme_backend.domain.media;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ImageUrl extends Timestamped {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MakerPost makerPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "singer_post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SingerPost singerPost;

    @Column(nullable = false)
    private String imageUrl;

    public void updateUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
}
