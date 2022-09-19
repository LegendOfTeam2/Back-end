package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.Member;
import com.example.rhythme_backend.domain.post.SingerPlayList;
import com.example.rhythme_backend.domain.post.SingerPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SingerPlayListRepository extends JpaRepository<SingerPlayList,Long> {

    List<SingerPlayList> findByMember(Member member);
    Long deleteAllBySingerPost(SingerPost singerPost);
    Long deleteBySingerPost(SingerPost singerPost);

}
