package com.example.airbnb.mappers;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.domain.entity.AccommodationImage;
import com.example.airbnb.domain.entity.Amenities;
import com.example.airbnb.domain.entity.Likes;
import com.example.airbnb.domain.entity.Review;
import com.example.airbnb.domain.entity.Tags;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AccommodationMapper {
    // 필터링 없는 숙소 전체 조회
    List<Accommodation> selectAllAccommodations();

    // destination 기준 숙소 조회
    List<Integer> selectAccommodationsByDestination(String destination);

    // 이미 예약되어 예약 불가능한 숙소 조회
    List<Integer> selectUnavailableAccommodationsByDate(LocalDate checkInDate,
                                                        LocalDate checkOutDate);

    // 날짜 기준 예약 불가능 숙소 조회 (다양한 겹침 케이스 포함)
    List<Integer> selectUnavailableAccommodations(LocalDate checkInDate,
                                                  LocalDate checkOutDate);

    // 숙소 상세 조회(단건)
    Accommodation selectAccommodationById(int id);

    //숙소 상세 조회(다건)
    List<Accommodation> selectAccommodationsByIds(String accommodations);

    // 숙소 생성
    int insertAccommodation(Accommodation accommodation);

    // 숙소 수정
    int updateAccommodation(Accommodation accommodation);

    // 숙소 삭제
    int deleteAccommodation(int accommodationId);

    // 숙소 이미지 등록
    int insertAccommodationImage(AccommodationImage accommodationImage);

    // 숙소 이미지 조회
    List<AccommodationImage> selectAccommodationImagesByAccommodationId(int accommodationId);

    // 숙소 태그 등록
    int insertAccommodationTag(Tags tags);

    // 숙소 태그 조회
    List<Tags> selectAccommodationTagsByAccommodationId(int accommodationId);

    // 편의시설 등록
    int insertAccommodationAmenity(Amenities amenities);

    // 편의시설 조회
    List<Amenities> selectAccommodationAmenitiesByAccommodationId(int accommodationId);

     // 리뷰 조회
     List<Review> selectReview(int accommodationId);

    // 좋아요 등록
    Likes insertAccommodationLike(Likes likes);

    // 숙소별 좋아요 수
    int selectLikeCountByAccommodation( int accommodationId);

    // 좋아요 많은 순으로 숙소 목록 반환
    List<Accommodation> selectAccommodationsOrderByLikeCount();

}
