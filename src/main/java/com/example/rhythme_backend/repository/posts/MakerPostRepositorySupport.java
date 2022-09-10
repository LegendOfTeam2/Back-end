package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.post.MakerPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;


@Repository
public class MakerPostRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MakerPostRepositorySupport(JPAQueryFactory queryFactory) {
        super(MakerPost.class);
        this.queryFactory = queryFactory;
    }
}
