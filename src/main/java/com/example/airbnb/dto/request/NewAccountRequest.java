package com.example.airbnb.dto.request;

import com.example.airbnb.domain.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewAccountRequest {
    @NotBlank
    String accountId;

    @NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*\\d).{7,20}")
    String pw;

    @NotBlank
    String name;

    @NotBlank
    @Email
    String email;

    @NotBlank
    String emailCode;

    String phoneNumber;
    String interestLocation;

    public Account toAccount(String pw) {
        Account account = new Account();
        account.setId(this.accountId);
        account.setPw(pw);
        account.setName(this.name);
        account.setEmail(this.email);
        account.setPhoneNumber(this.phoneNumber == null ? "" : this.phoneNumber);
        account.setInterestLocation(this.interestLocation == null ? "" : this.interestLocation);
        return account;
    }
}
