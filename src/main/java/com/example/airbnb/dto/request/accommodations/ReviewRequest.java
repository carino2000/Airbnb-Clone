package com.example.airbnb.dto.request.accommodations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewRequest {

    @NotNull(message = "별점(rating)을 입력해주세요.")
    double rating;
    @NotBlank(message = "리뷰 내용(content)을 입력해주세요.")
    String content;


}
