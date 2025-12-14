package com.example.airbnb.dto.response;

import com.example.airbnb.domain.entity.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageResponse {
    boolean success;
    String message;
    Message messageData;
}
