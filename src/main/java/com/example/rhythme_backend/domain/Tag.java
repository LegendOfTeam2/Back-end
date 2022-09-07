package com.example.rhythme_backend.domain;

import com.example.rhythme_backend.domain.post.MakerPostTag;
import com.example.rhythme_backend.domain.post.SingerPostTag;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "makerPostId")
    private List<MakerPostTag> makerPostTags;

    @OneToMany(mappedBy = "singerPostId")
    private List<SingerPostTag> singerPostTags;

    @OneToMany(mappedBy = "memberId")
    private List<MemberHashTag> memberTags;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

}
