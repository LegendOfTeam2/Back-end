package com.example.rhythme_backend.repository.posts;

import com.example.rhythme_backend.domain.post.MakerPost;
import com.example.rhythme_backend.dto.responseDto.post.MakerPostGetResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.rhythme_backend.domain.post.QMakerPost.makerPost;


@Repository
public class MakerPostRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MakerPostRepositorySupport(JPAQueryFactory queryFactory) {
        super(MakerPost.class);
        this.queryFactory = queryFactory;
    }

    //카테고리 별 조회
    public PageImpl<MakerPostGetResponseDto> findAll(Pageable pageable) {
        List<MakerPostGetResponseDto> content = queryFactory
                .select(Projections.fields(
                        MakerPostGetResponseDto.class,
                        makerPost.id,
                        makerPost.member,
                        makerPost.title,
                        makerPost.content,
                        makerPost.likes
//                        makerPost.lyrics
//                        makerPost.imageUrl,
//                        makerPost.mediaUrl
                ))
                .from(makerPost)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }
}
//
//    //전체 조회
//    public PageImpl<HouseMainResponseDto> findAllByOrderByModifiedAtDesc(Pageable pageable) {
//        List<HouseMainResponseDto> content = queryFactory
//                .select(Projections.fields(
//                        HouseMainResponseDto.class,
//                        house.houseId,
//                        house.category,
//                        house.title,
//                        house.nation,
//                        house.price,
//                        house.starAvg,
//                        houseImg.imgUrl
//                ))
//                .from(house)
//                .leftJoin(houseImg)
//                .on(house.houseId.eq(houseImg.house.houseId))
//                .groupBy(house.houseId)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//        return new PageImpl<>(content,pageable,content.size());
//    }
//
//    public PageImpl<HouseMainResponseDto> findAllByFilter(int minPrice, int maxPrice,
//                                                      int bedRoomCnt, int bedCnt, List<FacilityType> facilities, Pageable pageable) {
//
//        List<HouseMainResponseDto> content = queryFactory
//                .select(Projections.fields(
//                        HouseMainResponseDto.class,
//                        house.houseId,
//                        house.category,
//                        house.title,
//                        house.nation,
//                        house.price,
//                        house.starAvg,
//                        houseImg.imgUrl
//                ))
//                .from(house)
//                .leftJoin(facility)
//                .on(house.houseId.eq(facility.house.houseId))
//                .leftJoin(houseImg)
//                .on(house.houseId.eq(houseImg.house.houseId))
//                .where(betweenPrice(minPrice, maxPrice),
//                        eqBedRoomCnt(bedRoomCnt),
//                        eqBedCnt(bedCnt),
//                        eqFacilities(facilities))
//                .groupBy(house.houseId)
//                .orderBy(house.houseId.asc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        return new PageImpl<>(content,pageable,content.size());
//    }
//
//    private BooleanExpression betweenPrice(int minPrice, int maxPrice) {
//        if (minPrice == 0 && maxPrice == 0) return null;
//        return house.price.between(minPrice, maxPrice);
//    }
//
//    private BooleanExpression eqBedRoomCnt(int bedRoomCnt) {
//        if (bedRoomCnt == 0) return null;
//        return house.bedRoomCnt.eq(bedRoomCnt);
//    }
//
//    private BooleanExpression eqBedCnt(int bedCnt) {
//        if (bedCnt == 0) return null;
//        return house.bedRoomCnt.eq(bedCnt);
//    }
//
//    private BooleanExpression eqFacilities(List<FacilityType> facilities) {
//        if (facilities.isEmpty()) return null;
//        return facility.facilityType.in(facilities);
//    }
//}
