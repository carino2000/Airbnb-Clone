package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.Review;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationReviewByAmenitiesResponse {
    List<Review> review;

    boolean success;
}
