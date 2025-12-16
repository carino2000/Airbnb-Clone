package com.example.airbnb.dto.request.accommodations;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagsRequest {
    int id;
    int accommodationId;
    List<String> tags;


}
