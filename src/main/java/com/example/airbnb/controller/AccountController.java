package com.example.airbnb.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.airbnb.domain.entity.Account;
import com.example.airbnb.domain.entity.Verification;
import com.example.airbnb.dto.request.EditProfileRequest;
import com.example.airbnb.dto.request.LoginRequest;
import com.example.airbnb.dto.request.NewAccountRequest;
import com.example.airbnb.dto.response.AccountResponse;
import com.example.airbnb.mappers.AccountMapper;
import com.example.airbnb.mappers.VerificationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@CrossOrigin
public class AccountController {
    final AccountMapper accountMapper;
    final VerificationMapper verificationMapper;

    @PostMapping("/register")
    public AccountResponse createAccount(@RequestBody @Valid NewAccountRequest nar,
                                         BindingResult bindingResult) {
        AccountResponse resp = new AccountResponse();
        resp.setSuccess(false);

        if (bindingResult.hasErrors()) {
            resp.setMessage("정규식 오류 : " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            System.out.println("Error in createAccount: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return resp;
        }

        Verification verification = verificationMapper.selectLatestByEmail(nar.getEmail());
        if (verification == null) {
            resp.setMessage("email code can not found");
            return resp;
        }

        if (accountMapper.countDuplicateId(nar.getAccountId()) != 0) { // 닉네임 중복일 때
            resp.setMessage("duplicate nickname");
            System.out.println(resp.getMessage());
        } else if (!verification.getCode().equals(nar.getCode())) { // 인증코드 불일치
            resp.setMessage("verification code failed");
            System.out.println(resp.getMessage());
        } else if (verification.getExpiredAt().isBefore(LocalDateTime.now())) { // 인증코드 만료
            resp.setMessage("verification code expired");
            System.out.println(resp.getMessage());
        } else { // 통과! insert
            BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
            Account account = nar.toAccount(pwEncoder.encode(nar.getPw()));
            if (accountMapper.insertOne(account) == 1) {
                resp.setSuccess(true);
                resp.setMessage("account insert success");
                resp.setData(account);
            } else {
                resp.setMessage("account insert failed");
            }
        }
        return resp;
    }


    @PostMapping("/login")
    public AccountResponse login(@RequestBody LoginRequest lr) {
        AccountResponse resp = new AccountResponse();
        Account account = accountMapper.selectById(lr.getId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (account == null || !passwordEncoder.matches(lr.getPw(), account.getPw())) {
            resp.setSuccess(false);
            resp.setMessage("accountId or password is incorrect.");
        } else {
            Algorithm algorithm = Algorithm.HMAC256("f891c04f488d6c66cd4a5e4d9c8c0615");
            String token = JWT.create().withSubject(String.valueOf(account.getId())).withIssuedAt(new Date()).withIssuer("airbnb").sign(algorithm);
            resp.setSuccess(true);
            resp.setMessage("login successful.");
            resp.setData(account);
            resp.setToken(token);
        }
        return resp;
    }

    @PatchMapping("/{accountId}")
    public AccountResponse editProfile(@RequestBody @Valid EditProfileRequest epr, BindingResult bindingResult,
                                       @PathVariable String accountId) {
        AccountResponse resp = new AccountResponse();
        resp.setSuccess(false);
        if (bindingResult.hasErrors()) {
            resp.setMessage("정규식 오류 : " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            System.out.println("Error in editProfile: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return resp;
        }

        if (accountMapper.updateAccountInfo(epr.toAccount(accountId)) != 1) {
            resp.setMessage("editProfile Failed");
        } else {
            resp.setSuccess(true);
            resp.setMessage("edit profile successful.");
        }

        return resp;
    }

    @DeleteMapping("/{accountId}")
    public AccountResponse deleteAccount(@PathVariable String accountId) {
        AccountResponse resp = new AccountResponse();
        return resp;
    }


}
