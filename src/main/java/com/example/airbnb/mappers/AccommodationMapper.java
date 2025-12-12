package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Accommodation;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AccommodationMapper {
    int insertAccommodation(Accommodation accommodation);
    int updateAccommodation(Accommodation accommodation);

    List<Accommodation> selectAllAccommodations();
    Accommodation selectAccommodationById(int accommodationId);

}
