package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tags {
    int id;
    int accommodationId;
    String tag;
}
