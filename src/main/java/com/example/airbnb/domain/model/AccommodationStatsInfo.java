package com.example.airbnb.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationStatsInfo {
    String location;
    int accommodationCount;
    int averagePrice;
}
