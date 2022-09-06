package com.example.rhythme_backend.domain;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String hashtag;

    @OneToMany(mappedBy = "memberId")
    private List<MemberHashTag> memberTags;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

}
