package com.example.airbnb.dto.request;

import com.example.airbnb.domain.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EditProfileRequest {
    @NotBlank
    String id;
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;
    String phoneNumber;
    String interestLocation;

    public Account toAccount() {
        Account account = new Account();
        account.setId(this.id);
        account.setName(this.name);
        account.setEmail(this.email);
        account.setPhoneNumber(this.phoneNumber == null ? "" : this.phoneNumber);
        account.setInterestLocation(this.interestLocation == null ? "" : this.interestLocation);
        return account;
    }
}
