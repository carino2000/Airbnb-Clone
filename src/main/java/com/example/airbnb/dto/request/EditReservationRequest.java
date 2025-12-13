package com.example.airbnb.dto.request;

import com.example.airbnb.domain.model.ReservationDateParam;
import com.example.airbnb.domain.model.ReservationUpdateParam;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EditReservationRequest {
    @Positive
    int visitors;
    @NotNull
    LocalDate startDate;
    @NotNull
    LocalDate endDate;
    @PositiveOrZero
    int price;

    public ReservationDateParam toDateParam(int accommodationId){
        ReservationDateParam rpd = new ReservationDateParam();
        rpd.setAccommodationId(accommodationId);
        rpd.setStartDate(this.startDate);
        rpd.setEndDate(this.endDate);
        return rpd;
    }

    public ReservationUpdateParam toUpdateParam(String code){
        ReservationUpdateParam  rup = new ReservationUpdateParam();
        rup.setCode(code);
        rup.setVisitors(this.visitors);
        rup.setStartDate(this.startDate);
        rup.setEndDate(this.endDate);
        rup.setPrice(this.price);
        return rup;
    }
}
