package com.example.airbnb.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateResponse {
    Boolean duplicate;
    String message;
}
