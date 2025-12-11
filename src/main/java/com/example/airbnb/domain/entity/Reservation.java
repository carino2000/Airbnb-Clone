package com.example.airbnb.domain.entity;

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
}
