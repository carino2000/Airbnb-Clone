package com.example.airbnb.dto.response.reservations;

import com.example.airbnb.domain.model.ReservationDateParam;
import com.example.airbnb.dto.projection.MessageRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SelectMessageListResponse {
    boolean success;
    String message;
    List<MessageRoom> messageRooms;
}
