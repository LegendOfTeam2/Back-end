package com.example.rhythme_backend.domain.media;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.util.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MediaUrl extends Timestamped {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maker_post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MakerPost makerPost;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "singer_post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SingerPost singerPost;

    @Column(nullable = false)
    private String mediaUrl;

    public void updateUrl(String mediaUrl){
        this.mediaUrl = mediaUrl;
    }
}
