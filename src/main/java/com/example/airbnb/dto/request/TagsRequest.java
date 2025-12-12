package com.example.airbnb.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagsRequest {
    int id;
    int accommodationId;
    String tag;
}
