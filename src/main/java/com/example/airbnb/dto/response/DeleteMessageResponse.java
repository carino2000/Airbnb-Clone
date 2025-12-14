package com.example.airbnb.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMessageResponse {
    boolean success;
    String message;
}
