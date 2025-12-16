package com.example.airbnb.dto.response.accommodations;

import com.example.airbnb.domain.entity.Likes;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationSelectLikesResponse {

    Likes likes;

    int likesCount;

    boolean success;
}
