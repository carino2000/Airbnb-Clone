package com.example.airbnb.dto.request.accommodations;

import com.example.airbnb.domain.entity.Accommodation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccommodationEditRequest {
    String name;
    String description;
    int price;
    String address;
    double extraRate;
    int maxCapacity;
    int bedroom;
    int bed;
    int bathroom;

    List<String> tags;
    List<String> amenities;
    List<Integer> deleteImageId;

    public Accommodation toAccommodation(int accommodationId) {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationId);
        accommodation.setName(this.name);
        accommodation.setDescription(this.description);
        accommodation.setPrice(this.price);
        accommodation.setAddress(this.address);
        accommodation.setExtraRate(this.extraRate);
        accommodation.setMaxCapacity(this.maxCapacity);
        accommodation.setBedroom(this.bedroom);
        accommodation.setBed(this.bed);
        accommodation.setBathroom(this.bathroom);
        return accommodation;
    }
}
