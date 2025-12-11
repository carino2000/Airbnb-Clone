package com.example.airbnb.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Message {
    int id;
    String reservationCode;
    String recipientId;
    String writerId;
    String content;
    boolean readFlag;
    LocalDateTime writeAt;
}
