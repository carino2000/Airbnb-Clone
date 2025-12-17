package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Reservation;
import com.example.airbnb.domain.entity.ReservationDate;
import com.example.airbnb.domain.model.ReservationDateParam;
import com.example.airbnb.domain.model.ReservationUpdateParam;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReservationMapper {
    int insertOne (Reservation reservation);
    int insertReservationDate(ReservationDate reservationDate);
    Reservation selectOne(String code);
    int countDuplicateDate(ReservationDateParam params);

    int deleteReservationByCode(String code);
    int deleteReservationDate(ReservationDateParam params);
    int updateReservation(ReservationUpdateParam params);



    // 예약 불가 일자(숙소별 이미 예약된 날짜) 조회
    List<LocalDate> selectReservationsDateByAccommodationId(int accommodationId);

}
