package com.example.airbnb.dto.request.reservations;

import com.example.airbnb.domain.entity.Reservation;
import com.example.airbnb.domain.model.ReservationDateParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NewReservationRequest {
    @Positive
    int accommodationId;
    @NotBlank
    String accountId;
    @Positive
    int visitors;
    @NotNull
    LocalDate startDate;
    @NotNull
    LocalDate endDate;
    @PositiveOrZero
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
