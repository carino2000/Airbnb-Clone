package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.domain.entity.AccommodationImage;
import com.example.airbnb.domain.entity.Amenities;
import com.example.airbnb.domain.entity.Likes;
import com.example.airbnb.domain.entity.Review;
import com.example.airbnb.domain.entity.Tags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccommodationMapper {
    // 숙소 전체 조회
    List<Accommodation> selectAllAccommodations();

    // 숙소 상세 조회
    Accommodation selectAccommodationById(int id);

    // 숙소 생성
    int insertAccommodation(Accommodation accommodation);

    // 숙소 수정
    int updateAccommodation(Accommodation accommodation);

    // 숙소 삭제
    int deleteAccommodation(int id);

    // 숙소 이미지 등록
    int insertAccommodationImage(AccommodationImage accommodationImage);

    int insertAccommodationTag(Tags tags);

    // 편의시설 등록 (XML expects an Amenities object)
    int insertAccommodationAmenity(Amenities amenities);

    // 특정 편의시설에 달린 리뷰 조회 (XML uses amenityId)
    List<Review> selectReviewsByAmenity(@Param("amenityId") int amenityId);

    // 좋아요 조회 (XML expects map with accommodationId and userId)
    List<Likes> selectAccommodationLike(@Param("accommodationId") int accommodationId,
                                       @Param("userId") int userId);

    // 좋아요 등록 (XML expects a Likes object)
    int insertAccommodationLike(Likes likes);

}
