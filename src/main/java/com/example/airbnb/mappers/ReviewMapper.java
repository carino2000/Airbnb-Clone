package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {

    // 리뷰 작성
    int insertReview(Review review);

    List<Review> selectReviewByAccommodation(int accommodationId);

    // 리뷰 삭제
    int deleteReview(int reviewId);

    // 숙소별 별점 평균 조회
    Double selectAverageRatingByAccommodationId(int accommodationId);

}
