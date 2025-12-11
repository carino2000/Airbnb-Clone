package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationImage {
    int id;
    int accommodationId;
    String uri;
}
