package com.example.airbnb.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class LikesResponse {
    int id;
    String accountId;
    int accommodationId;

    public LikesResponse(){
    }
}
