package com.example.airbnb.dto.response.reservations;

import com.example.airbnb.domain.entity.Message;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectMessagesResponse {

    List<Message> messages;
    boolean success;
}

