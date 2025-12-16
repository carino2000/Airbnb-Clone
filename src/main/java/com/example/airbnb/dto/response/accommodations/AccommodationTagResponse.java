package com.example.airbnb.dto.response.accommodations;

import com.example.airbnb.domain.entity.Tags;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationTagResponse {
    List<Tags> tags;

    boolean success;
}
