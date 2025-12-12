package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Likes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikesMapper {
    int insert(Likes like);
}
