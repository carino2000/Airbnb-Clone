package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Reservation;
import com.example.airbnb.domain.entity.ReservationDate;
import com.example.airbnb.domain.model.ReservationDateParam;
import com.example.airbnb.domain.model.ReservationUpdateParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {
    int insertOne (Reservation reservation);
    int insertReservationDate(ReservationDate reservationDate);
    Reservation selectOne(String code);
    List<Reservation> selectReservationByAccommodationId(int accommodationId);

    int countDuplicateDate(ReservationDateParam params);

    int deleteReservationByCode(String code);
    int deleteReservationDate(ReservationDateParam params);
    int updateReservation(ReservationUpdateParam params);
}
