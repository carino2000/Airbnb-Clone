package com.example.airbnb.dto.request.accommodations;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewRequest {

    double rating;
    String content;


}
