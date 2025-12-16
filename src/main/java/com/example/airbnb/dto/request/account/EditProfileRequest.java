package com.example.airbnb.dto.request.account;

import com.example.airbnb.domain.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileRequest {
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;
    String phoneNumber;
    String interestLocation;

    public Account toAccount(String accountId) {
        Account account = new Account();
        account.setId(accountId);
        account.setName(this.name);
        account.setEmail(this.email);
        account.setPhoneNumber(this.phoneNumber == null ? "" : this.phoneNumber);
        account.setInterestLocation(this.interestLocation == null ? "" : this.interestLocation);
        return account;
    }
}
