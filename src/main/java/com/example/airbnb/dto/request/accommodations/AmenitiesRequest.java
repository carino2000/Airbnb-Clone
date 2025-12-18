package com.example.airbnb.dto.request.accommodations;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AmenitiesRequest {

    @NotEmpty(message = "편의 시설 목록(amenities)을 입력해주세요.")
    List<String> amenities;
}
