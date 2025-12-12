package com.example.airbnb.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EditReservationRequest {
    @NotBlank
    int visitors;
    @NotBlank
    LocalDate startDate;
    @NotBlank
    LocalDate endDate;
    @NotBlank
    int price;
}
