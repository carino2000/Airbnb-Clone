package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Likes {
    int id;
    String accountId;
    int accommodationId;
}
