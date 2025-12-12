package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.dto.request.AccommodationImageRequest;
import com.example.airbnb.dto.request.AccommodationRequest;
import com.example.airbnb.dto.response.AccommodationResponse;
import com.example.airbnb.mappers.AccommodationMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {
    private final AccommodationMapper accommodationMapper;

    // 숙소 전체 조회
    @GetMapping("/accommodations")
    public String getAllAccommodations() {
        return "숙소 전체 조회";
    }


    // 숙소 상세 조회
    @GetMapping("/accommodations/{accommodationId}")
    public String getAccommodationDetails() {
        return "숙소 상세 조회";
    }

    // 숙소 생성
    @PostMapping("/accommodations")
    public AccommodationResponse createAccommodation(@RequestBody AccommodationRequest accommodationRequest) {

        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationRequest.getId());
        accommodation.setHostId(accommodationRequest.getHostId());
        accommodation.setName(accommodationRequest.getName());
        accommodation.setDescription(accommodationRequest.getDescription());
        accommodation.setPrice(accommodationRequest.getPrice());
        accommodation.setAddress(accommodationRequest.getAddress());
        accommodation.setExtraRate(accommodationRequest.getExtraRate());
        accommodation.setMaxCapacity(accommodationRequest.getMaxCapacity());
        accommodation.setBedroom(accommodationRequest.getBedroom());
        accommodation.setBed(accommodationRequest.getBed());
        accommodation.setBathroom(accommodationRequest.getBathroom());

        int inserted = accommodationMapper.insert(accommodation);

        AccommodationResponse accommodationResponse = AccommodationResponse.builder()
                .id(accommodation.getId())
                .hostId(accommodation.getHostId())
                .name(accommodation.getName())
                .description(accommodation.getDescription())
                .price(accommodation.getPrice())
                .address(accommodation.getAddress())
                .extraRate(accommodation.getExtraRate())
                .maxCapacity(accommodation.getMaxCapacity())
                .bedroom(accommodation.getBedroom())
                .bed(accommodation.getBed())
                .bathroom(accommodation.getBathroom())
                .success(true)
                .build();

        return accommodationResponse;
    }

    // 숙소 수정
    @PutMapping("/accommodations/{accommodationId}")
    public String updateAccommodation() {
        return "숙소 수정";
    }

    //숙소 삭제
    @DeleteMapping("/accommodations/{accommodationId}")
    public String deleteAccommodation() {
        return "숙소 삭제";
    }

    //숙소 이미지 등록
    @PostMapping("/accommodations/{accommodationId}/images")
    public String uploadAccommodationImages() {
        return "숙소 이미지 등록";
    }

    // 숙소 태그 등록
    @PostMapping("/accommodations/{accommodationId}/tags")
    public String addAccommodationTags() {
        return "숙소 태그 등록";
    }

    // 편의시설 등록
    @PostMapping("/accommodations/{accommodationId}/amenities")
    public String addAccommodationAmenities() {
        return "편의시설 등록";
    }

    // 특정 편의시설에 달린 리뷰 주회
    @GetMapping("/accommodations/{accommodationId}/reviews")
    public String getAccommodationReviews() {
        return "특정 편의시설에 달린 리뷰 조회";
    }



}
