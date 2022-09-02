package com.example.rhythme_backend.domain.post;

import com.example.rhythme_backend.domain.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SingerPostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "singer_post_id")
    private SingerPost singer_post_id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag_id;

    public SingerPostTag (SingerPost singerPost,Tag tag){
        this.singer_post_id = singerPost;
        this.tag_id = tag;
    }
}
