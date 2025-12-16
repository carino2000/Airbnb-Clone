package com.example.airbnb.dto.response.accommodations.data;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.domain.entity.AccommodationImage;
import com.example.airbnb.domain.entity.Amenities;
import com.example.airbnb.domain.entity.Tags;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDetail {
    int id;
    String hostId;
    String name;
    String description;
    int price;
    String address;
    double extraRate;
    int maxCapacity;
    int bedroom;
    int bed;
    int bathroom;

    List<AccommodationImage> images;
    List<Tags> tags;
    int likes;
    List<Amenities> amenities;

    public static AccommodationDetail fromEntity(Accommodation accommodation) {
        return AccommodationDetail.builder()
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
                .build();
    }
}
