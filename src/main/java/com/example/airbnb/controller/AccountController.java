package com.example.airbnb.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.airbnb.domain.entity.Account;
import com.example.airbnb.domain.entity.Verification;
import com.example.airbnb.dto.request.EditPasswordRequest;
import com.example.airbnb.dto.request.EditProfileRequest;
import com.example.airbnb.dto.request.LoginRequest;
import com.example.airbnb.dto.request.NewAccountRequest;
import com.example.airbnb.dto.response.AccountLoginResponse;
import com.example.airbnb.dto.response.AccountResponse;
import com.example.airbnb.mappers.AccountMapper;
import com.example.airbnb.mappers.VerificationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@CrossOrigin
public class AccountController {
    final AccountMapper accountMapper;
    final VerificationMapper verificationMapper;

    // 신규 회원가입
    @PostMapping("/register")
    public AccountResponse createAccount(@RequestBody @Valid NewAccountRequest nar,
                                              BindingResult bindingResult) {
        AccountResponse resp = new AccountResponse();
        resp.setSuccess(false);

        if (bindingResult.hasErrors()) {
            FieldError fe = bindingResult.getFieldError();
            if (fe != null) {
                resp.setMessage(fe.getField() + " 필드 오류: " + fe.getDefaultMessage());
            } else {
                resp.setMessage("요청 값 오류: " + bindingResult.getAllErrors().getFirst().getDefaultMessage());
            }
            return resp;
        }


        Verification verification = verificationMapper.selectLatestByEmail(nar.getEmail());
        if (verification == null) {
            resp.setMessage("email code can not found");
            return resp;
        }

        if (accountMapper.countDuplicateId(nar.getAccountId()) != 0) { // 아이디 중복일 때
            resp.setMessage("duplicate Id");
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

    // 로그인
    @PostMapping("/login")
    public AccountLoginResponse login(@RequestBody LoginRequest lr) {
        AccountLoginResponse resp = new AccountLoginResponse();
        Account account = accountMapper.selectById(lr.getId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(lr.getId());
        System.out.println(lr.getPw());

        if (account == null || !passwordEncoder.matches(lr.getPw(), account.getPw())) {
            resp.setSuccess(false);
            resp.setMessage("accountId or password is incorrect.");
        } else {
            Algorithm algorithm = Algorithm.HMAC256("f891c04f488d6c66cd4a5e4d9c8c0615");
            String token = JWT.create()
                    .withSubject(account.getId())
                    .withIssuedAt(new Date())
                    .withIssuer("airbnb")
                    .sign(algorithm);
            resp.setSuccess(true);
            resp.setMessage("login successful.");
            resp.setData(account);
            resp.setToken(token);
        }
        return resp;
    }

    // 회원 정보 수정
    @PutMapping("/{accountId}")
    public AccountResponse editProfile(@RequestBody @Valid EditProfileRequest epr, BindingResult bindingResult,
                                            @PathVariable String accountId,
                                            @RequestAttribute String tokenId) {
        AccountResponse resp = new AccountResponse();
        resp.setSuccess(false);
        if (!tokenId.equals(accountId)) {
            resp.setMessage("invalid token");
            return resp;
        }

        if (bindingResult.hasErrors()) {
            FieldError fe = bindingResult.getFieldError();
            if (fe != null) {
                resp.setMessage(fe.getField() + " 필드 오류: " + fe.getDefaultMessage());
            } else {
                resp.setMessage("요청 값 오류: " + bindingResult.getAllErrors().getFirst().getDefaultMessage());
            }
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

    // 회원 탈퇴
    @DeleteMapping("/{accountId}")
    public AccountResponse deleteAccount(@PathVariable String accountId,
                                              @RequestAttribute String tokenId) {
        AccountResponse resp = new AccountResponse();
        resp.setSuccess(false);

        if (!tokenId.equals(accountId)) {
            resp.setMessage("invalid token");
            return resp;
        }

        Account account = accountMapper.selectById(accountId);
        if (account == null) {
            resp.setMessage("account not found");
        } else {
            if (accountMapper.deleteAccountById(accountId) != 1) {
                resp.setMessage("delete account failed");
            } else {
                resp.setSuccess(true);
                resp.setMessage("delete successful.");
            }
        }

        return resp;
    }

    // 계정 비밀번호 변경
    @PutMapping("/{accountId}/password")
    public AccountResponse editPassword(@PathVariable String accountId,
                                             @RequestAttribute String tokenId,
                                             @RequestBody @Valid EditPasswordRequest epr, BindingResult bindingResult) {
        AccountResponse resp = new AccountResponse();
        resp.setSuccess(false);

        if (!tokenId.equals(accountId)) {
            resp.setMessage("invalid token");
            return resp;
        }

        if (bindingResult.hasErrors()) {
            FieldError fe = bindingResult.getFieldError();
            if (fe != null) {
                resp.setMessage(fe.getField() + " 필드 오류: " + fe.getDefaultMessage());
            } else {
                resp.setMessage("요청 값 오류: " + bindingResult.getAllErrors().getFirst().getDefaultMessage());
            }
            return resp;
        }


        Account account = accountMapper.selectById(accountId);
        if (account == null) {
            resp.setMessage("account not found");
            return resp;
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(epr.getOldPw(), account.getPw())) {
            resp.setMessage("old password is incorrect.");
        } else if (epr.getOldPw().equals(epr.getNewPw())) {
            resp.setMessage("already using password.");
        } else if (epr.getNewPw().equals(epr.getNewPwConfirm())) {
            resp.setMessage("confirm password does not match.");
        } else {
            Map<String, String> map = Map.of("id", accountId, "pw", passwordEncoder.encode(epr.getNewPw()));
            if (accountMapper.updateAccountPw(map) != 1) {
                resp.setMessage("update password failed.");
            } else {
                resp.setSuccess(true);
                resp.setMessage("update password successful.");
            }
        }

        return resp;
    }
}
