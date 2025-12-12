package com.example.airbnb.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TagsResponse {
    int id;
    int accommodationId;
    String tag;

    public TagsResponse(){
    }
}
