package com.example.airbnb.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagsRequest {
    List<String> tags;
}
