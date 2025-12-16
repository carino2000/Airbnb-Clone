package com.example.airbnb.dto.response.accommodations;

import com.example.airbnb.domain.entity.Accommodation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationMostLikesResponse {

    boolean success;
    List<Accommodation> mostLikedAccommodations;
}