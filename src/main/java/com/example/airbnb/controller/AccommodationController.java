package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.domain.entity.Tags;
import com.example.airbnb.domain.entity.Likes;
import com.example.airbnb.domain.entity.Amenities;
import com.example.airbnb.domain.entity.AccommodationImage;
import com.example.airbnb.dto.request.AccommodationRequest;
import com.example.airbnb.dto.response.accommodation.*;
import com.example.airbnb.mappers.AccommodationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {
    private final AccommodationMapper accommodationMapper;

    // 숙소 전체 조회(검색)
    @GetMapping
    public AccommodationSelectAllResponse selectAllAccommodations(@RequestParam (required = false) String Destination,
                                                                  @RequestParam (required = false) LocalDate CheckInDate,
                                                                  @RequestParam (required = false) LocalDate CheckOutDate,
                                                                  @RequestParam (required = false) Integer Guests) {

        List<Accommodation> accommodations = accommodationMapper.selectAllAccommodations();

        return AccommodationSelectAllResponse.builder().success(true).accommodations(accommodations).build();
    }

    // 숙소 상세 조회
    @GetMapping("/{accommodationId}")
    public AccommodationSelectByIdResponse selectAccommodationById(@PathVariable int accommodationId) {

        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);
        if (accommodation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "조회된 숙소가 없습니다");
        }

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

        // NOTE: hostId must exist in account table due to FK constraint
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
        if (accommodation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "수정할 숙소가 없습니다");
        }

        accommodation.setName(accommodationRequest.getName());
        accommodation.setDescription(accommodationRequest.getDescription());
        accommodation.setPrice(accommodationRequest.getPrice());
        accommodation.setAddress(accommodationRequest.getAddress());
        accommodation.setExtraRate(accommodationRequest.getExtraRate());
        accommodation.setMaxCapacity(accommodationRequest.getMaxCapacity());
        accommodation.setBedroom(accommodationRequest.getBedroom());
        accommodation.setBed(accommodationRequest.getBed());
        accommodation.setBathroom(accommodationRequest.getBathroom());

        accommodationMapper.updateAccommodation(accommodation);

        return AccommodationEditResponse.builder()
                .accommodation(accommodation)
                .success(true)
                .build();
    }

    //숙소 삭제
    @DeleteMapping("/{accommodationId}")
    public AccommodationDeleteResponse deleteAccommodation(@PathVariable int accommodationId) {
        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);

        accommodationMapper.deleteAccommodation(accommodationId);

        return AccommodationDeleteResponse.builder()
                .accommodation(accommodation)
                .success(true)
                .build();
    }

    //숙소 이미지 등록 (URI 리스트 형태로 받음)
    @PostMapping("/{accommodationId}/images")
    public AccommodationImageResponse uploadAccommodationImages(@PathVariable int accommodationId,
                                                                 @RequestBody List<String> imageUris) {
        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);
        if (accommodation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 숙소가 없습니다");
        }

        AccommodationImage firstSaved = null;
        for (String uri : imageUris) {
            AccommodationImage image = new AccommodationImage();
            image.setAccommodationId(accommodationId);
            image.setUri(uri);
            accommodationMapper.insertAccommodationImage(image);
            if (firstSaved == null) firstSaved = image;
        }

        return AccommodationImageResponse.builder()
                .image(firstSaved)
                .success(true)
                .build();
    }

    // 숙소 태그 등록
    @PostMapping("/{accommodationId}/tags")
    public AccommodationTagResponse addAccommodationTags(@PathVariable int accommodationId,
                                                         @RequestBody List<String> tagsRequest) {

        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);
        if (accommodation == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 숙소가 없습니다");

        List<Tags> savedTags = new ArrayList<>();
        for (String tagText : tagsRequest) {
            Tags tags = new Tags();
            tags.setAccommodationId(accommodationId);
            tags.setTag(tagText);
            accommodationMapper.insertAccommodationTag(tags);
            savedTags.add(tags);
        }

        return AccommodationTagResponse.builder()
                .tags(savedTags)
                .success(true)
                .build();
    }

    // 편의시설 등록
    @PostMapping("/{accommodationId}/amenities")
    public AccommodationAmenitiesResponse addAccommodationAmenities(@PathVariable int accommodationId,
                                                                    @RequestBody List<String> amenitiesRequest) {

        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);
        if (accommodation == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 숙소가 없습니다");

        List<Amenities> savedAmenities = new ArrayList<>();
        for (String amenityText : amenitiesRequest) {
            Amenities amenities = new Amenities();
            amenities.setAccommodationId(accommodationId);
            amenities.setAmenity(amenityText);
            accommodationMapper.insertAccommodationAmenity(amenities);
            savedAmenities.add(amenities);
        }

        return AccommodationAmenitiesResponse.builder()
                .amenities(savedAmenities)
                .success(true)
                .build();
    }

    // 특정 편의시설에 달린 리뷰 조회 (amenityId를 쿼리로 전달)
    @GetMapping("/{accommodationId}/reviews")
    public AccommodationReviewByAmenitiesResponse getAccommodationReviews(@PathVariable int accommodationId,
                                                                         @RequestParam int amenityId) {
        // 간단히 amenityId로 리뷰 조회
        List<com.example.airbnb.domain.entity.Review> reviews = accommodationMapper.selectReviewsByAmenity(amenityId);
        return AccommodationReviewByAmenitiesResponse.builder()
                .review(reviews)
                .success(true)
                .build();
    }

    //좋아요 조회
    @GetMapping("/likes")
    public AccommodationSelectLikesResponse getAccommodationLikes(@RequestParam int accommodationId,
                                                                  @RequestParam int userId) {
        List<Likes> likes = accommodationMapper.selectAccommodationLike(accommodationId, userId);
        return AccommodationSelectLikesResponse.builder()
                .likes(likes)
                .success(true)
                .build();
    }

    //좋아요 등록
    @PostMapping("/{accommodationId}/likes")
    public AccommodationInsertLikesResponse addAccommodationLike(@PathVariable int accommodationId,
                                                                 @RequestBody Map<String, String> body) {
        String accountId = body.get("accountId");
        if (accountId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "accountId required");

        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);
        if (accommodation == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 숙소가 없습니다");

        Likes likes = new Likes();
        likes.setAccommodationId(accommodationId);
        likes.setAccountId(accountId);
        accommodationMapper.insertAccommodationLike(likes);

        return AccommodationInsertLikesResponse.builder()
                .likes(likes)
                .success(true)
                .build();
    }


}
