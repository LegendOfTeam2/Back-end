package com.example.rhythme_backend.repository;

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






    






//    //카테고리 별 조회
//    public List<HouseMainResponseDto> findAllByCategory(Category category) {
//        return queryFactory
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
//                .where(house.category.eq(category))
//                .groupBy(house.houseId)
//                .fetch();
//    }
//
//
//
//    public List<HouseMainResponseDto> findAllByFilter(int minPrice, int maxPrice,
//                                                      int bedRoomCnt, int bedCnt, List<FacilityType> facilities) {
//
//        return queryFactory
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
//                .orderBy(house.houseId.desc())
//                .groupBy(house.houseId)
//                .fetch();
//
//
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
}
