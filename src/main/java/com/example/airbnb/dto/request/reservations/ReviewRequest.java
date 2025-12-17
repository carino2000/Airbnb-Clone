package com.example.airbnb.dto.request.reservations;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewRequest {
    int accommodationId;
    String accountId;
    String reservationCode;
    double rating;
    String content;
}
