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
}
