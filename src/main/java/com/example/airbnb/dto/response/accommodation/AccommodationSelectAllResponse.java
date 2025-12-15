package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.*;
import com.example.airbnb.dto.response.accommodation.data.AccommodationDetail;
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
