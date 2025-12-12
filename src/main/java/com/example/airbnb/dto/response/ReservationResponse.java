package com.example.airbnb.dto.response;

import com.example.airbnb.domain.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationResponse {
    boolean success;
    String message;
    List<Reservation> data;

    public void setData(Reservation reservation) {
        this.data.add(reservation);
    }
}
