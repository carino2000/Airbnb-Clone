package com.example.airbnb.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckReservationResponse {
    boolean success;
    String message;
    boolean reservationAvailable;
    int totalPrice;
}
