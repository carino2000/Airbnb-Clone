package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.dto.request.AccommodationRequest;
import com.example.airbnb.dto.response.accommodation.AccommodationEditResponse;
import com.example.airbnb.dto.response.accommodation.AccommodationSelectAllResponse;
import com.example.airbnb.dto.response.accommodation.AccommodationCreateResponse;
import com.example.airbnb.dto.response.accommodation.AccommodationSelectByIdResponse;
import com.example.airbnb.dto.response.accommodation.TagsResponse;
import com.example.airbnb.mappers.AccommodationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {
    private final AccommodationMapper accommodationMapper;

    // 숙소 전체 조회(검색)
    @GetMapping
    public AccommodationSelectAllResponse selectAllAccommodations(@RequestParam (required = false) String name,
                                                                  @RequestParam (required = false) Integer priceMin,
                                                                  @RequestParam (required = false) Integer priceMax,
                                                                  @RequestParam (required = false) String address) {

        List<Accommodation> accommodations = accommodationMapper.selectAllAccommodations();

        return AccommodationSelectAllResponse.builder().success(true).accommodations(accommodations).build();
    }


    // 숙소 상세 조회
    @GetMapping("/{accommodationId}")
    public AccommodationSelectByIdResponse selectAccommodationById(@PathVariable int accommodationId) {

        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);

        return AccommodationSelectByIdResponse.builder()
                .accommodation(accommodation)
                .success(true)
                .build();



    }

    // 숙소 생성
    @PostMapping
    public AccommodationCreateResponse createAccommodation(@RequestBody AccommodationRequest accommodationRequest) {

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

        return AccommodationCreateResponse.builder()
                .accommodation(accommodation)
                .success(true)
                .build();
    }

    // 숙소 수정
    @PutMapping("/{accommodationId}")
    public AccommodationEditResponse updateAccommodation(@PathVariable int accommodationId,
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

        return AccommodationEditResponse.builder()
                .accommodation(accommodation)
                .success(true)
                .build();
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

    /*// 숙소 태그 등록
    @PostMapping("/{accommodationId}/tags")
    public String addAccommodationTags(@PathVariable int accommodationId,
                                       @RequestBody List<String> tagsRequest) {

        return TagsResponse.builder()
                .accommodationId(accommodationId);
                .tag(tagsRequest)
                .success(true)
                .build()
                .toString();
    }
*/
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
