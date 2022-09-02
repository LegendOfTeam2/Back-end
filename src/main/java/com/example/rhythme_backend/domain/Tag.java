package com.example.rhythme_backend.domain;

import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.SingerPost;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String tag;

    @ManyToOne
    @JoinColumn(name = "maker_post")
    private MakerPost maker_post;

    @ManyToOne
    @JoinColumn(name = "singer_post")
    private SingerPost singer_post;

}
