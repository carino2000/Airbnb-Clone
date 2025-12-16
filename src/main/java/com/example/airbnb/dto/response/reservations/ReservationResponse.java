package com.example.airbnb.dto.response.reservations;

import com.example.airbnb.domain.entity.Reservation;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    boolean success;
    String message;
    Reservation data;
}
