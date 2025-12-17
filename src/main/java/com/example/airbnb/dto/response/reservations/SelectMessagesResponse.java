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
    boolean success;
    String message;
    List<Message> messages;
}

