package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationSelectByIdResponse {

    Accommodation accommodation;

    List<AccommodationImage> accommodationImages;
    List<Tags> tags;
    List<Amenities> amenities;
    List<Likes> likes;


    boolean success;
}
