package com.example.airbnb.dto.request.accommodations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

@Getter
@Setter
public class AccommodationRequest {

    int id;

    @NotBlank(message = "호스트 아이디(hostId)를 입력해주세요.")
    String hostId;
    @NotBlank(message = "숙소 이름(name)을 입력해주세요.")
    String name;
    @NotBlank(message = "숙소 설명(description)을 입력해주세요.")
    String description;
    @NotNull(message = "가격(price)을 입력해주세요.")
    int price;
    @NotBlank(message = "주소(address)를 입력해주세요.")
    String address;

    double extraRate;

    @NotNull(message = "최대 수용 인원(maxCapacity)을 입력해주세요.")
    int maxCapacity;
    @NotNull(message = "방 수(bedroom)를 입력해주세요.")
    int bedroom;
    @NotNull(message = "침대 수(bed)를 입력해주세요.")
    int bed;
    @NotNull(message = "욕실 수(bathroom)를 입력해주세요.")
    int bathroom;
}
