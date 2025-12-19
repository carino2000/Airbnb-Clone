package com.example.airbnb.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationAndAccommodationAndImage {
    String code;
    int accommodationId;
    String accountId;
    int visitors;
    LocalDate startDate;
    LocalDate endDate;
    int price;
    String name;
    String description;
    String address;
    String imageUri;
}
