package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.Accommodation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationSelectAllResponse {
    List<Accommodation> accommodations;

    boolean success;
}
