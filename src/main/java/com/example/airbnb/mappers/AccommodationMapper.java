package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Accommodation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccommodationMapper {
    int insert(Accommodation accommodation);
}
