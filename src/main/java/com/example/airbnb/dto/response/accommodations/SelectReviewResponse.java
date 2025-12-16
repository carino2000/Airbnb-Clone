package com.example.airbnb.dto.response.accommodations;

import com.example.airbnb.domain.entity.Review;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectReviewResponse {

    List<Review> review;

    boolean success;
}
