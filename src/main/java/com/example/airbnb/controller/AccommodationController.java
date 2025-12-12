package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.dto.request.AccommodationRequest;
import com.example.airbnb.dto.response.AccommodationResponse;
import com.example.airbnb.mappers.AccommodationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {
    private final AccommodationMapper accommodationMapper;

    // 숙소 전체 조회
    @GetMapping
    public List<AccommodationResponse> selectAllAccommodations(@RequestParam (required = false) String name,
                                       @RequestParam (required = false) Integer priceMin,
                                       @RequestParam (required = false) Integer priceMax,
                                       @RequestParam (required = false) String address) {

        List<Accommodation> accommodations = accommodationMapper.selectAllAccommodations();


        List<AccommodationResponse> accommodationResponse = new ArrayList<>();
        for (Accommodation a : accommodations) {
            AccommodationResponse resp = AccommodationResponse.builder()
                    .id(a.getId())
                    .hostId(a.getHostId())
                    .name(a.getName())
                    .description(a.getDescription())
                    .price(a.getPrice())
                    .address(a.getAddress())
                    .extraRate(a.getExtraRate())
                    .maxCapacity(a.getMaxCapacity())
                    .bedroom(a.getBedroom())
                    .bed(a.getBed())
                    .bathroom(a.getBathroom())
                    .success(true)
                    .build();
            accommodationResponse.add(resp);
        }

        return accommodationResponse;
    }


    // 숙소 상세 조회
    @GetMapping("/{accommodationId}")
    public AccommodationResponse selectAccommodationById(@PathVariable int accommodationId) {

        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);

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

    // 숙소 생성
    @PostMapping
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

        accommodationMapper.insertAccommodation(accommodation);

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
    @PutMapping("/{accommodationId}")
    public AccommodationResponse updateAccommodation(@PathVariable int accommodationId,
                                      @RequestBody AccommodationRequest accommodationRequest) {


        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);


        accommodation.setName(accommodationRequest.getName());
        accommodation.setDescription(accommodationRequest.getDescription());
        accommodation.setPrice(accommodationRequest.getPrice());
        accommodation.setAddress(accommodationRequest.getAddress());
        accommodation.setExtraRate(accommodationRequest.getExtraRate());
        accommodation.setMaxCapacity(accommodationRequest.getMaxCapacity());
        accommodation.setBedroom(accommodationRequest.getBedroom());
        accommodation.setBed(accommodationRequest.getBed());
        accommodation.setBathroom(accommodationRequest.getBathroom());

        int updated = accommodationMapper.updateAccommodation(accommodation);

        AccommodationResponse updatedResponse = AccommodationResponse.builder()
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
                .success(updated > 0)
                .build();

        return updatedResponse;
    }

    //숙소 삭제
    @DeleteMapping("/{accommodationId}")
    public String deleteAccommodation() {
        return "숙소 삭제";
    }

    //숙소 이미지 등록
    @PostMapping("/{accommodationId}/images")
    public String uploadAccommodationImages() {
        return "숙소 이미지 등록";
    }

    // 숙소 태그 등록
    @PostMapping("/{accommodationId}/tags")
    public String addAccommodationTags() {
        return "숙소 태그 등록";
    }

    // 편의시설 등록
    @PostMapping("/{accommodationId}/amenities")
    public String addAccommodationAmenities() {
        return "편의시설 등록";
    }

    // 특정 편의시설에 달린 리뷰 주회
    @GetMapping("/{accommodationId}/reviews")
    public String getAccommodationReviews() {
        return "특정 편의시설에 달린 리뷰 조회";
    }

    //좋아요 조회
    @GetMapping("/likes")
    public String getAccommodationLikes() {
        return "좋아요 조회";
    }

    //좋아요 등록
    @PostMapping("/{accommodationId}/likes")
    public String addAccommodationLike() {
        return "좋아요 등록";
    }


}
