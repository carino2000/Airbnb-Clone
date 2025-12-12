package com.example.airbnb.domain.entity;

import com.example.airbnb.domain.model.ReservationDateParam;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Reservation {
    String code;
    int accommodationId;
    String accountId;
    int visitors;
    LocalDate startDate;
    LocalDate endDate;
    int price;

    public ReservationDateParam toParam() {
        ReservationDateParam rdp = new ReservationDateParam();
        rdp.setAccommodationId(accommodationId);
        rdp.setStartDate(startDate);
        rdp.setEndDate(endDate);
        return rdp;
    }
}
