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
public class MakerPostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maker_post_id")
    private MakerPost makerPostId;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tagId;

    public MakerPostTag(MakerPost makerPost,Tag tag){
        this.makerPostId = makerPost;
        this.tagId = tag;
    }
}
