package com.example.airbnb.dto.response.accommodation;

import com.example.airbnb.domain.entity.Accommodation;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDeleteResponse {
    Accommodation accommodation;

    boolean success;
}
