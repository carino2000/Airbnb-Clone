package com.example.airbnb.dto.request.accommodations;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagsRequest {

    @NotEmpty(message = "태그 목록(tags)을 입력해 주세요.")
    List<String> tags;
}
