package com.example.airbnb.domain.entity;

import com.example.airbnb.domain.model.AccommodationAndImage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
// 해석 : 클래스의 equals() 및 hashCode() 메서드를 자동으로 생성해줍니다. 이를 통해 객체 비교 및 해시 기반 컬렉션에서의 동작이 올바르게 이루어지도록 합니다.
public class Accommodation {
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

    public AccommodationAndImage toAccommodationAndImage(String image) {
        AccommodationAndImage accommodation = new AccommodationAndImage();
        accommodation.setId(id);
        accommodation.setHostId(hostId);
        accommodation.setName(name);
        accommodation.setDescription(description);
        accommodation.setPrice(price);
        accommodation.setAddress(address);
        accommodation.setExtraRate(extraRate);
        accommodation.setMaxCapacity(maxCapacity);
        accommodation.setBedroom(bedroom);
        accommodation.setBed(bed);
        accommodation.setBathroom(bathroom);
        accommodation.setUri(image);
        return accommodation;
    }
}
