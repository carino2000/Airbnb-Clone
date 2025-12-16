package com.example.airbnb.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPasswordRequest {
    @NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*\\d).{7,20}")
    String oldPw;
    @NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*\\d).{7,20}")
    String newPw;
    @NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*\\d).{7,20}")
    String newPwConfirm;
}
