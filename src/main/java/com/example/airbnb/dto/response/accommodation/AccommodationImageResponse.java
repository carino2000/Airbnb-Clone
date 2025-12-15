package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.AccommodationImage;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationImageResponse {

    List<AccommodationImage> image;

    boolean success;


}
