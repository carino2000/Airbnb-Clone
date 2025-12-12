package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationDate {
    int id;
    int accommodationId;
    LocalDate date;

    public ReservationDate(int accommodationId, LocalDate date) {
        this.accommodationId = accommodationId;
        this.date = date;
    }
}
