package com.example.airbnb.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class AccommodationResponse {
    int id;
    String hostId;
    String name;
    String description;
    int price;
    String address;
    double extraRate;
    int maxCapacity;
    int bedroom;
    int bed;
    int bathroom;

    boolean success;

    public AccommodationResponse() {
    }
}
