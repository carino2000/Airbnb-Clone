package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Review;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewMapper {
    int insert(Review review);
}
