package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AccommodationImage {
    int id;
    int accommodationId;
    String uri;
}
