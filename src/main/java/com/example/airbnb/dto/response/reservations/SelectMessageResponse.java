package com.example.airbnb.dto.response.reservations;

import com.example.airbnb.domain.entity.Message;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectMessageResponse {

    Message message;
    boolean success;
}
