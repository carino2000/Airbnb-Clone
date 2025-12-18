package com.example.airbnb.dto.response.accommodations;

import com.example.airbnb.domain.model.AccommodationStatsInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccommodationStatsInfoResponse {
    boolean success;
    String message;
    int totalCount;
    List<AccommodationStatsInfo> accommodationStatisticalInfoList;
}
