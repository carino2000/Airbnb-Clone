package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.Amenities;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationAmenitiesResponse {
    List<Amenities> amenities;

    boolean success;
}
