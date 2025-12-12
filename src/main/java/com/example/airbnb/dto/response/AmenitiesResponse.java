package com.example.airbnb.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmenitiesResponse {
    int id;
    int accommodationId;
    String amenity;


}
