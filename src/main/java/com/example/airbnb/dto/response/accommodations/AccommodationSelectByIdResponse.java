package com.example.airbnb.dto.response.accommodations;

import com.example.airbnb.dto.response.accommodations.data.AccommodationDetail;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationSelectByIdResponse {

    AccommodationDetail accommodation;


    boolean success;
}
