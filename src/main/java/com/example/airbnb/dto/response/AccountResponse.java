package com.example.airbnb.dto.response;

import com.example.airbnb.domain.entity.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse {
    boolean success;
    String message;
    Account data;
    String token;
}
