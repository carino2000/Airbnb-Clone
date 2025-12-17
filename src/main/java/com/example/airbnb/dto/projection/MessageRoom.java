package com.example.airbnb.dto.projection;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageRoom {
    String reservationCode;
    String recipientId;
    String lastMessage;
    LocalDateTime lastReceiveTime;
    int unReadCount;
}
