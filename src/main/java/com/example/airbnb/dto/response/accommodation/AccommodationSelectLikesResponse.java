package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.domain.entity.Likes;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationSelectLikesResponse {

    List<Likes> likes;

    boolean success;
}
