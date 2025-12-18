package com.example.airbnb.dto.request.accommodations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikesRequest {
    int id;

    @NotBlank(message = "계정 아이디(accountId)를 입력해주세요.")
    String accountId;

    // PathVariable로 받기 때문에 NotNull 제거
    int accommodationId;
}
