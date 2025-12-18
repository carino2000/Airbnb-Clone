package com.example.airbnb.dto.request.reservations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewRequest {

    int accommodationId;
    String accountId;

    @NotBlank(message = "예약코드(reservationCode)를 입력해주세요.")
    String reservationCode;
    @NotNull(message = "평점(rating)을 입력해주세요.")
    @Size(min = 1, max = 5, message = "평점(rating)은 1에서 5 사이의 값이어야 합니다.")
    Double rating;
    @NotBlank(message = "내용(content)을 입력해주세요.")
    String content;
}
