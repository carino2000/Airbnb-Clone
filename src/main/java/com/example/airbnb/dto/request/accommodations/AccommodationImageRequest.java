package com.example.airbnb.dto.request.accommodations;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class AccommodationImageRequest {

    @NotEmpty(message = "이미지(images)를 등록해주세요.")
    List<MultipartFile> images;
}
