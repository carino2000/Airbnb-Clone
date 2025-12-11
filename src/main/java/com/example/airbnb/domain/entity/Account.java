package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Account {
    String id;
    String pw;
    String name;
    String email;
    LocalDateTime joinAt;
    String phoneNumber;
    String interestLocation;
}
