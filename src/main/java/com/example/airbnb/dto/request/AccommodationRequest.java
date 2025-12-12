package com.example.airbnb.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationRequest {
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
}
