package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.*;
import com.example.airbnb.dto.request.AccommodationImageRequest;
import com.example.airbnb.dto.request.AccommodationRequest;
import com.example.airbnb.dto.request.LikesRequest;
import com.example.airbnb.dto.response.accommodation.*;
import com.example.airbnb.mappers.AccommodationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {
    final AccommodationMapper accommodationMapper;

    // 검색 필터링 or 필터링 없는 숙소 전체 조회
    @GetMapping
    public AccommodationSelectAllResponse selectAllAccommodations(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) LocalDate checkInDate,
            @RequestParam(required = false) LocalDate checkOutDate,
            @RequestParam(required = false) Integer guests
    ) {

        List<Integer> accommodations = new ArrayList<>();

        if (destination != null && !destination.isBlank()) {
            List<Integer> idsByDestination
                    = accommodationMapper.selectAccommodationsByDestination(destination);
            accommodations.addAll(idsByDestination);

        } else {
            List<Accommodation> all = accommodationMapper.selectAllAccommodations();
            for (Accommodation accommodation : all) {
                accommodations.add(accommodation.getId());
            }
        }

        if (guests != null && guests > 0) {
            List<Integer> fits = new ArrayList<>();
            List<Accommodation> all = accommodationMapper.selectAllAccommodations();
            for (Accommodation accommodation : all) {
                if (accommodation.getMaxCapacity() >= guests) {
                    fits.add(accommodation.getId());
                }
            }
            accommodations.retainAll(fits);
        }

        if (checkInDate != null && checkOutDate != null) {
            List<Integer> reserved
                    = accommodationMapper.selectUnavailableAccommodationsByDate(checkInDate, checkOutDate);
            if (reserved != null && !reserved.isEmpty()) {
                accommodations.removeAll(reserved);
            }
        }

        List<Accommodation> accommodationList;

        if (accommodations.isEmpty()) {
            accommodationList = new ArrayList<>();
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < accommodations.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(accommodations.get(i));
            }
            String ids = sb.toString();
            accommodationList = accommodationMapper.selectAccommodationsByIds(ids);

        }

        List<AccommodationImage> images = new ArrayList<>();
        List<Tags> tagList = new ArrayList<>();
        List<Amenities> amenityList = new ArrayList<>();
        List<Likes> likesList = new ArrayList<>();

        if (accommodationList != null && !accommodationList.isEmpty()) {
            for (Accommodation accommodation : accommodationList) {

                int accommodationId = accommodation.getId();

                accommodationMapper.selectAccommodationImagesByAccommodationId(accommodationId);
                accommodationMapper.selectAccommodationTagsByAccommodationId(accommodationId);
                accommodationMapper.selectAccommodationAmenitiesByAccommodationId(accommodationId);
                accommodationMapper.selectLikeCountByAccommodation(accommodationId);

            }
        }

        return AccommodationSelectAllResponse.builder()
                .accommodations(accommodationList)
                .accommodationImages(images)
                .tags(tagList)
                .amenities(amenityList)
                .likes(likesList)
                .success(true)
                .build();
    }

    // 숙소 상세 조회
    @GetMapping("/{accommodationId}")
    public AccommodationSelectByIdResponse selectAccommodationById(@PathVariable int accommodationId) {

        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);

        List<AccommodationImage> images = new ArrayList<>();
        List<Tags> tagList = new ArrayList<>();
        List<Amenities> amenityList = new ArrayList<>();
        List<Likes> likesList = new ArrayList<>();

        if (accommodation != null) {

            accommodationMapper.selectAccommodationImagesByAccommodationId(accommodationId);
            accommodationMapper.selectAccommodationTagsByAccommodationId(accommodationId);
            accommodationMapper.selectAccommodationAmenitiesByAccommodationId(accommodationId);
            accommodationMapper.selectLikeCountByAccommodation(accommodationId);

        }


        return AccommodationSelectByIdResponse.builder()
                .accommodation(accommodation)
                .accommodationImages(images)
                .tags(tagList)
                .amenities(amenityList)
                .likes(likesList)
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

        accommodationMapper.updateAccommodation(accommodation);

        return AccommodationEditResponse.builder()
                .accommodation(accommodation)
                .success(true)
                .build();
    }

    // 숙소 삭제
    @DeleteMapping("/{accommodationId}")
    public AccommodationDeleteResponse deleteAccommodation(@PathVariable int accommodationId) {

        int deleted = accommodationMapper.deleteAccommodation(accommodationId);

        return AccommodationDeleteResponse.builder()
                .accommodation(deleted)
                .success(true)
                .build();
    }

    // 숙소 이미지 등록
    @PostMapping(value = "/{accommodationId}/images")
    public AccommodationImageResponse uploadAccommodationImages(@PathVariable int accommodationId,
                                                                @ModelAttribute AccommodationImageRequest accommodationImageRequest
    ) throws IOException {

        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);
        if (accommodation == null) {
            return AccommodationImageResponse.builder().image(new ArrayList<>()).success(false).build();
        }


        List<AccommodationImage> savedImages = new ArrayList<>();

        List<MultipartFile> uri = accommodationImageRequest.getUri();

        if (uri != null && !uri.isEmpty()) {

            Path uplodadPath = Path.of(System.getProperty("user.home"), "accommodations", "images");
            Files.createDirectories(uplodadPath);

            for (MultipartFile file : uri) {
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");

                Path filePath = uplodadPath.resolve(uuid);
                file.transferTo(filePath.toFile());

                String imageUri = "/accommodation/images/" + uuid;

                AccommodationImage accommodationImage = new AccommodationImage();
                accommodationImage.setAccommodationId(accommodationId);
                accommodationImage.setUri(imageUri);

                accommodationMapper.insertAccommodationImage(accommodationImage);
                savedImages.add(accommodationImage);
            }
        }
        return AccommodationImageResponse.builder()
                .image(savedImages)
                .success(true)
                .build();
    }

    // 숙소 태그 등록
    @PostMapping("/{accommodationId}/tags")
    public AccommodationTagResponse addAccommodationTags(@PathVariable int accommodationId,
                                                         @RequestBody List<String> tagsRequest) {
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
        List<Amenities> savedAmenities = new ArrayList<>();

        for (String amenity : amenitiesRequest) {

            Amenities amenities = new Amenities();
            amenities.setAccommodationId(accommodationId);
            amenities.setAmenity(amenity);

            accommodationMapper.insertAccommodationAmenity(amenities);

            savedAmenities.add(amenities);

        }

        return AccommodationAmenitiesResponse.builder()
                .amenities(savedAmenities)
                .success(true)
                .build();
    }

   /* // 특정 숙소에 달린 리뷰 조회

    @GetMapping("/{accommodationId}/reviews")
    public AccommodationReviewByAmenitiesResponse getAccommodationReviews(@PathVariable int accommodationId,
                                                                          @RequestParam int amenityId) {

        List<Review> reviews = accommodationMapper.selectReviewsByAmenity(accommodationId, amenityId);


        return AccommodationReviewByAmenitiesResponse.builder()
                .review(reviews)
                .success(true)
                .build();
    }*/

    //좋아요 등록
    @PostMapping("/{accommodationId}/likes")
    public AccommodationSelectLikesResponse addAccommodationLike(@PathVariable int accommodationId,
                                                                 @RequestBody LikesRequest likesRequest) {

        String accountId = likesRequest.getAccountId();

        Likes likes = new Likes();
        likes.setAccommodationId(accommodationId);
        likes.setAccountId(accountId);

        accommodationMapper.insertAccommodationLike(likes);

        return AccommodationSelectLikesResponse.builder()
                .success(true)
                .build();
    }

    // 숙소별 좋아요 수
    @GetMapping("/{accommodationId}/likes")
    public AccommodationSelectLikesResponse getAccommodationLikes(@PathVariable int accommodationId) {


        int likesCount = accommodationMapper.selectLikeCountByAccommodation(accommodationId);


        return AccommodationSelectLikesResponse.builder()
                .likesCount(likesCount)
                .success(true)
                .build();
    }

    // 좋아요 많은 순으로 숙소 목록 반환
    @GetMapping("/likes/most")
    public AccommodationMostLikesResponse getAccommodationsOrderByLikeCount() {

        List<Accommodation> rows = accommodationMapper.selectAccommodationsOrderByLikeCount();

        return AccommodationMostLikesResponse.builder()

                .success(true)
                .mostLikedAccommodations(rows)
                .build();
    }
}

