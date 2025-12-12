package com.example.airbnb.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmenitiesRequest {
    int id;
    int accommodationId;
    String amenity;
}
