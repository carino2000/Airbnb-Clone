package com.example.airbnb.dto.response.accommodations;

import com.example.airbnb.dto.response.accommodations.data.AccommodationDetail;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationSelectAllResponse {

   List<AccommodationDetail> accommodations;
    int total;
    boolean success;
}
