package com.example.airbnb.dto.request.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    String accountId;
    String pw;
}
