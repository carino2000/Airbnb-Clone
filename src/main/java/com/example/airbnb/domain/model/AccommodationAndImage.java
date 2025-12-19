package com.example.airbnb.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationAndImage {
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

    String uri;
}
