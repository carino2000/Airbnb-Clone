package com.example.airbnb.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikesResponse {
    int id;
    String accountId;
    int accommodationId;


}
