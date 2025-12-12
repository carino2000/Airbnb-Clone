package com.example.airbnb.dto.response.accommodation;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationImageResponse {
    int id;
    int accommodationId;
    String uri;


}
