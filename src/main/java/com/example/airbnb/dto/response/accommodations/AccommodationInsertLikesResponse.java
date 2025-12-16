package com.example.airbnb.dto.response.accommodations;


import com.example.airbnb.domain.entity.Likes;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationInsertLikesResponse {

    Likes likes;

    int likesCount;

    boolean success;
}
