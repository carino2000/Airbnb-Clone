package com.example.airbnb.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagsResponse {
    int id;
    int accommodationId;
    String tag;


}
