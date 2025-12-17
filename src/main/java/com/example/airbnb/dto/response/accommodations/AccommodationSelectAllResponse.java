package com.example.airbnb.dto.response.accommodations;

import com.example.airbnb.dto.response.accommodations.data.AccommodationDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"success", "total", "accommodations"})
public class AccommodationSelectAllResponse {

   List<AccommodationDetail> accommodations;
    int total;
    boolean success;
}
