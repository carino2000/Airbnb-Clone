package com.example.airbnb.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class AccommodationImageRequest {
    List<MultipartFile> images;
}
