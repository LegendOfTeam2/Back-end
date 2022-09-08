package com.example.rhythme_backend.domain.post;

import com.example.rhythme_backend.domain.Member;
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
    private SingerPost singerPostId;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tagId;

    public SingerPostTag (SingerPost singerPost,Tag tag){
        this.singerPostId = singerPost;
        this.tagId = tag;
    }
}
