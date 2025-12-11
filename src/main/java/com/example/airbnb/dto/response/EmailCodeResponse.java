package com.example.airbnb.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailCodeResponse {
    String code;
    LocalDateTime expiredAt;

    public EmailCodeResponse(String code, LocalDateTime expiredAt) {
        this.code = code;
        this.expiredAt = expiredAt;
    }
}
