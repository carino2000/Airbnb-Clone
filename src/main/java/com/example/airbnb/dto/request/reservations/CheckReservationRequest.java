package com.example.airbnb.dto.request.reservations;

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
public class CheckReservationRequest {
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

    public ReservationDateParam toParam() {
        ReservationDateParam rpd = new ReservationDateParam();
        rpd.setAccommodationId(this.accommodationId);
        rpd.setStartDate(this.startDate);
        rpd.setEndDate(this.endDate);
        return rpd;
    }
}
