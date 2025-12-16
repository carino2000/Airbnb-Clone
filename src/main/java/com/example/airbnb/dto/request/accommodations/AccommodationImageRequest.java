package com.example.airbnb.dto.request.accommodations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class AccommodationImageRequest {
    int id;
    int accommodationId;
    List<MultipartFile> uri;
}
