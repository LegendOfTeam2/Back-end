package com.example.rhythme_backend.domain;

import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.domain.post.MakerPostTag;
import com.example.rhythme_backend.domain.post.SingerPost;
import com.example.rhythme_backend.domain.post.SingerPostTag;
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

    @OneToMany(mappedBy = "maker_post_id")
    private List<MakerPostTag> makerPostTags;

    @OneToMany(mappedBy = "singer_post_id")
    private List<SingerPostTag> singerPostTags;

}
