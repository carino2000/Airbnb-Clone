package com.example.airbnb.dto.response.accommodations;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.domain.model.AccommodationAndImage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SelectHostingListResponse {
    boolean success;
    String message;
    List<AccommodationAndImage> accommodations;
}
