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
public class MediaUrl extends Timestamped {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column
    private Long postId;

    @Column
    private String category;

    @JsonIgnore
    @Column
    private String position;

    @Column(nullable = false)
    private String mediaUrl;

    public void updateUrl(String mediaUrl){
        this.mediaUrl = mediaUrl;
    }
}
