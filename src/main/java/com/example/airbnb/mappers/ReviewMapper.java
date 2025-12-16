package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

    // 리뷰 작성
    int insertReview(Review review);

    // 리뷰 삭제
    int deleteReview(int reviewId);

}
