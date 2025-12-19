package com.example.airbnb.dto.response.reservations;

import com.example.airbnb.domain.model.ReservationAndAccommodationAndImage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SelectReservationListResponse {
    boolean success;
    String message;
    List<ReservationAndAccommodationAndImage> reservations;
}
