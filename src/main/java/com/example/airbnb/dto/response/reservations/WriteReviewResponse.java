package com.example.airbnb.dto.response.reservations;

import com.example.airbnb.domain.entity.Review;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriteReviewResponse {

    Review review;
    boolean success;
}
