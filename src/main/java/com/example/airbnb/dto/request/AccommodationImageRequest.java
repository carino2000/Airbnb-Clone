package com.example.airbnb.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationImageRequest {
    int id;
    int accommodationId;
    String uri;
}
