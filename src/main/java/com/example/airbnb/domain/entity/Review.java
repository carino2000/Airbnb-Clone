package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Review {
    int id;
    String accountId;
    String reservationCode;
    double rating;
    LocalDateTime writeAt;
    String content;
}
