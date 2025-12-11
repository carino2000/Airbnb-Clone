package com.example.airbnb.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterResponse {
    boolean success;
    String message;

    public FilterResponse(boolean success) {
        this.success = success;
    }
}
