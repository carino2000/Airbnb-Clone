package com.example.airbnb.dto.request.reservations;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewRequest {
    int id;
    String accountId;
    String reservationCode;
    double rating;
    LocalDateTime writeAt;
    String content;

}
