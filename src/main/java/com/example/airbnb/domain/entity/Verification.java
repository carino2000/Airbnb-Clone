package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Verification {
    String idx;
    String email;
    String code;
    LocalDateTime expiredAt;
}
