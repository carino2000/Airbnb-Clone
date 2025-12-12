package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.domain.entity.Likes;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationInsertLikesResponse {

    Likes likes;

    boolean success;
}
