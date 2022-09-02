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
    private MakerPost maker_post_id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag_id;

    public MakerPostTag(MakerPost makerPost,Tag tag){
        this.maker_post_id = makerPost;
        this.tag_id = tag;
    }
}
