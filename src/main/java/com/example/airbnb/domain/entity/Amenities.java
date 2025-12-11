package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Amenities {
    int id;
    int accommodationId;
    String amenity;
}
