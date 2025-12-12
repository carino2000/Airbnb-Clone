package com.example.airbnb.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikesRequest {
    int id;
    String accountId;
    int accommodationId;
}
