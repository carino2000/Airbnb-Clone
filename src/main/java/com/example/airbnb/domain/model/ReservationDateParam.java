package com.example.airbnb.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationDateParam {
    int accommodationId;
    LocalDate startDate;
    LocalDate endDate;
}
