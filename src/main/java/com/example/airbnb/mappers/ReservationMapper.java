package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Reservation;
import com.example.airbnb.domain.entity.ReservationDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationMapper {
    int insertOne (Reservation reservation);
    int insertReservationDate(ReservationDate reservationDate);
    Reservation selectOne(String code);
}
