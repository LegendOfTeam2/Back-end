package com.example.rhythme_backend.domain.media;


import com.example.rhythme_backend.util.Timestamped;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ImageUrl extends Timestamped {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long postId;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String imageUrl;

    public void updateUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
}
