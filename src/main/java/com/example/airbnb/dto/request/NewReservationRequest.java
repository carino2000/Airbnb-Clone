package com.example.airbnb.dto.request;

import com.example.airbnb.domain.entity.Reservation;
import com.example.airbnb.domain.model.ReservationDateParam;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NewReservationRequest {
    @NotBlank
    int accommodationId;
    @NotBlank
    String accountId;
    @NotBlank
    int visitors;
    @NotBlank
    LocalDate startDate;
    @NotBlank
    LocalDate endDate;
    @NotBlank
    int price;

    public ReservationDateParam toParam() {
        ReservationDateParam rpd = new ReservationDateParam();
        rpd.setAccommodationId(this.accommodationId);
        rpd.setStartDate(this.startDate);
        rpd.setEndDate(this.endDate);
        return rpd;
    }

    public Reservation toReservation(String code) {
        Reservation reservation = new Reservation();
        reservation.setCode(code);
        reservation.setAccommodationId(this.accommodationId);
        reservation.setAccountId(this.accountId);
        reservation.setVisitors(this.visitors);
        reservation.setStartDate(this.startDate);
        reservation.setEndDate(this.endDate);
        reservation.setPrice(this.price);
        return reservation;
    }
}
