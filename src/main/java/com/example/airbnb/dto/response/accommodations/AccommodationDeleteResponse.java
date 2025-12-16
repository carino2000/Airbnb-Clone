package com.example.airbnb.dto.response.accommodations;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDeleteResponse {
    int accommodation;

    boolean success;
}
