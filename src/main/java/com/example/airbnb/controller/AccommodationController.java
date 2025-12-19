package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.*;
import com.example.airbnb.domain.model.AccommodationAndImage;
import com.example.airbnb.domain.model.AccommodationStatsInfo;
import com.example.airbnb.dto.request.accommodations.*;
import com.example.airbnb.dto.response.accommodations.*;
import com.example.airbnb.dto.response.accommodations.data.AccommodationDetail;
import com.example.airbnb.mappers.AccommodationMapper;
import com.example.airbnb.mappers.AccountMapper;
import com.example.airbnb.mappers.ReservationMapper;
import com.example.airbnb.mappers.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodations")
public class AccommodationController {
    final AccommodationMapper accommodationMapper;
    final AccountMapper accountMapper;
    final ReservationMapper reservationMapper;
    private final ReviewMapper reviewMapper;

    // 검색 필터링 or 필터링 없는 숙소 전체 조회
    @GetMapping
    public AccommodationSelectAllResponse selectAllAccommodations(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) LocalDate checkInDate,
            @RequestParam(required = false) LocalDate checkOutDate,
            @RequestParam(required = false) Integer guests
    ) {

        List<Accommodation> accommodations = accommodationMapper.selectAllAccommodations();


        // 목적지를 만족하는 데이터 조회
        if (destination != null && !destination.isBlank()) {
            List<Accommodation> accommodationsByArea
                    = accommodationMapper.selectAccommodationsByDestination(destination);
            accommodations.retainAll(accommodationsByArea);
        }

        // 인원수를 만족하는 데이터 조회
        if (guests != null && guests > 0) {
            List<Accommodation> accommodationsByGuest = accommodationMapper.selectAccommodationsByCapacity(guests);
            accommodations.retainAll(accommodationsByGuest);
        }

        // 예약(날짜)이 가능한 데이터 조회
        if (checkInDate != null && checkOutDate != null) {
            List<Accommodation> accommodationsByDuration = accommodationMapper.selectAccommodationsByDuration(checkInDate, checkOutDate);
            accommodations.retainAll(accommodationsByDuration);
        }


        List<AccommodationDetail> allSelect = new ArrayList<>();
        for (Accommodation accommodation : accommodations) {
            AccommodationDetail detail = AccommodationDetail.fromEntity(accommodation);

            int accommodationId = accommodation.getId();
            List<AccommodationImage> images = accommodationMapper.selectAccommodationImagesByAccommodationId(accommodationId);
            List<Tags> tags = accommodationMapper.selectAccommodationTagsByAccommodationId(accommodationId);
            List<Amenities> amenities = accommodationMapper.selectAccommodationAmenitiesByAccommodationId(accommodationId);
            int likes = accommodationMapper.selectLikeCountByAccommodation(accommodationId);

            detail.setImages(images);
            detail.setTags(tags);
            detail.setAmenities(amenities);
            detail.setLikes(likes);

            allSelect.add(detail);
        }


        return AccommodationSelectAllResponse.builder()
                .accommodations(allSelect)
                .success(true)
                .total(allSelect.size())
                .build();
    }

    // 숙소 상세 조회
    @GetMapping("/{accommodationId}")
    public AccommodationSelectByIdResponse selectAccommodationById(@PathVariable int accommodationId) {


        Accommodation accommodation = accommodationMapper.selectAccommodationById(accommodationId);

        AccommodationDetail accommodationDetail = AccommodationDetail.fromEntity(accommodation);


        List<AccommodationImage> accommodationImages = accommodationMapper.selectAccommodationImagesByAccommodationId(accommodationId);
        List<Tags> tags = accommodationMapper.selectAccommodationTagsByAccommodationId(accommodationId);
        List<Amenities> amenities = accommodationMapper.selectAccommodationAmenitiesByAccommodationId(accommodationId);
        int likes = accommodationMapper.selectLikeCountByAccommodation(accommodationId);

        Account account = accountMapper.selectById(accommodation.getHostId());

        List<LocalDate> reservedDate = reservationMapper.selectReservationsDateByAccommodationId(accommodationId);

        Double averageRating = reviewMapper.selectAverageRatingByAccommodationId(accommodationId);
        if (averageRating == null) {
            averageRating = 0.0;
        }


        accommodationDetail.setImages(accommodationImages);
        accommodationDetail.setTags(tags);
        accommodationDetail.setAmenities(amenities);
        accommodationDetail.setLikes(likes);

        accommodationDetail.setHostJoinAt(account.getJoinAt());
        accommodationDetail.setReservedDate(reservedDate);
        accommodationDetail.setAverageRating(averageRating);


        return AccommodationSelectByIdResponse.builder()
                .accommodation(accommodationDetail)
                .success(true)
                .build();
    }


    // 숙소 생성
    @PostMapping
    public AccommodationCreateResponse createAccommodation(@RequestBody AccommodationRequest accommodationRequest) {
        Accommodation accommodation = new Accommodation();
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
                                                         @RequestBody AccommodationEditRequest aer) {

        // -------------------- 숙소 수정 --------------------
        accommodationMapper.updateAccommodation(aer.toAccommodation(accommodationId));

        // -------------------- 숙소 태그 정보 수정 --------------------
        accommodationMapper.deleteAccommodationTags(accommodationId);
        for (String tag : aer.getTags()) {
            Tags tags = new Tags();
            tags.setAccommodationId(accommodationId);
            tags.setTag(tag);
            accommodationMapper.insertAccommodationTag(tags);
        }

        // -------------------- 숙소 편의시설 정보 수정 --------------------
        accommodationMapper.deleteAccommodationAmenities(accommodationId);
        for (String amenity : aer.getAmenities()) {
            Amenities amenities = new Amenities();
            amenities.setAccommodationId(accommodationId);
            amenities.setAmenity(amenity);
            accommodationMapper.insertAccommodationAmenity(amenities);
        }

        // -------------------- 숙소 이미지 수정(삭제) --------------------
        for(int i : aer.getDeleteImageId()){
            accommodationMapper.deleteAccommodationImage(i);
        }

        return AccommodationEditResponse.builder()
                .accommodation(aer.toAccommodation(accommodationId))
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

        List<MultipartFile> uri = accommodationImageRequest.getImages();

        if (uri != null && !uri.isEmpty()) {

            Path uplodadPath = Path.of(System.getProperty("user.home"), "accommodation", "images");
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
                                                         @RequestBody TagsRequest tagsRequest) {
        List<Tags> savedTags = new ArrayList<>();
        for (String tag : tagsRequest.getTags()) {

            Tags tags = new Tags();
            tags.setAccommodationId(accommodationId);
            tags.setTag(tag);

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
                                                                    @RequestBody AmenitiesRequest amenitiesRequest) {
        List<Amenities> savedAmenities = new ArrayList<>();

        for (String amenity : amenitiesRequest.getAmenities()) {

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

    // 리뷰 조회
    @GetMapping("/{accommodationId}/reviews")
    public SelectReviewResponse getAccommodationReviews(@PathVariable int accommodationId) {

        List<Review> reviews = reviewMapper.selectReviewByAccommodation(accommodationId);

        return SelectReviewResponse.builder()
                .review(reviews)
                .success(true)
                .build();
    }

    //아이디 별 좋아요 조회
    @GetMapping("/{accountId}/likes/list")
    public AccommodationMyLikesResponse selectMyAccommodationLikes(@PathVariable String accountId) {
        AccommodationMyLikesResponse resp = new AccommodationMyLikesResponse();
        resp.setSuccess(true);

        List<Likes> savedLikes = accommodationMapper.selectMyLikesByAccountId(accountId);
        List<Integer> likesIds = new ArrayList<>();
        List<AccommodationAndImage> accommodations = new ArrayList<>();
        for (Likes likes : savedLikes) {
            likesIds.add(likes.getAccommodationId());
            Accommodation accommodation = accommodationMapper.selectAccommodationById(likes.getAccommodationId());
            List<AccommodationImage> list = accommodationMapper.selectAccommodationImagesByAccommodationId(likes.getAccommodationId());
            accommodations.add(accommodation.toAccommodationAndImage(list.getFirst().getUri()));
        }

        resp.setMessage("select my likes successfully");
        resp.setAccommodationId(likesIds);
        resp.setAccommodations(accommodations);

        return resp;
    }

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

    //좋아요 취소
    @DeleteMapping("/{accommodationId}/likes")
    public AccommodationLikeDeleteResponse deleteAccommodationLike(@PathVariable int accommodationId,
                                                                   @RequestBody LikesRequest likesRequest) {
        AccommodationLikeDeleteResponse resp = new AccommodationLikeDeleteResponse();

        Likes likes = new Likes();
        likes.setAccommodationId(accommodationId);
        likes.setAccountId(likesRequest.getAccountId());
        accommodationMapper.deleteAccommodationLike(likes);

        resp.setSuccess(true);
        resp.setMessage("like delete success");

        return resp;
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

    @GetMapping("/{accountId}/hosting")
    public SelectHostingListResponse getHostingList(@PathVariable String accountId) {
        SelectHostingListResponse resp = new SelectHostingListResponse();
        resp.setSuccess(true);
        List<AccommodationAndImage> list = accommodationMapper.selectAccommodationAndImageByHostId(accountId);
        if (list.isEmpty()) {
            resp.setMessage("accommodation not found");
            resp.setAccommodations(null);
        } else {
            resp.setMessage("get accommodation list success");
            resp.setAccommodations(list);
        }
        return resp;
    }

    @GetMapping("/stats-info")
    public AccommodationStatsInfoResponse getAccommodationStatsInfo() {
        AccommodationStatsInfoResponse resp = new AccommodationStatsInfoResponse();

        resp.setSuccess(true);
        resp.setMessage("get accommodation stats info success");
        resp.setAccommodationStatisticalInfoList(accommodationMapper.selectAccommodationStatsInfo());
        int totalCount = 0;
        for(AccommodationStatsInfo info : resp.getAccommodationStatisticalInfoList()) {
            totalCount += info.getAccommodationCount();
        }
        resp.setTotalCount(totalCount);

        return resp;
    }
}
