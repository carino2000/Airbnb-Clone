package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.AccommodationImage;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationImageResponse {

    AccommodationImage image;

    boolean success;


}
