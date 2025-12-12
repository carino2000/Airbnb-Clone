package com.example.airbnb.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class AccommodationImageResponse {
    int id;
    int accommodationId;
    String uri;

    public AccommodationImageResponse(){

    }
}
