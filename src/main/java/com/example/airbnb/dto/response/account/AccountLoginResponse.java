package com.example.airbnb.dto.response.account;

import com.example.airbnb.domain.entity.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountLoginResponse {
    boolean success;
    String message;
    Account data;
    String token;
}
