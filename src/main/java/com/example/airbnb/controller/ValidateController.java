package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.Verification;
import com.example.airbnb.dto.request.account.NewEmailCodeRequest;
import com.example.airbnb.dto.response.DuplicateResponse;
import com.example.airbnb.dto.response.EmailCodeResponse;
import com.example.airbnb.mappers.AccountMapper;
import com.example.airbnb.mappers.VerificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
@CrossOrigin
public class ValidateController {
    final AccountMapper accountMapper;
    final VerificationMapper verificationMapper;
    final JavaMailSender mailSender;

    // 아이디 중복체크
    @GetMapping("/id")
    public DuplicateResponse checkDuplicateId(@RequestParam(required = false) String id) {
        DuplicateResponse resp = new DuplicateResponse();
        if (accountMapper.countDuplicateId(id) == 1) {
            resp.setDuplicate(true);
            resp.setMessage("Duplicate ID");
        } else {
            resp.setDuplicate(false);
            resp.setMessage("Available ID");
        }
        return resp;
    }

    // 이메일 중복체크
    @GetMapping("/email")
    public DuplicateResponse checkDuplicateEmail(@RequestParam(required = false) String email) {
        DuplicateResponse resp = new DuplicateResponse();
        if (accountMapper.countDuplicateEmail(email) == 1) {
            resp.setDuplicate(true);
            resp.setMessage("Duplicate Email");
        } else {
            resp.setDuplicate(false);
            resp.setMessage("Available Email");
        }
        return resp;
    }

    // 이메일 인증코드 발송
    @PostMapping("/code")
    public EmailCodeResponse sendEmailCode(@RequestBody NewEmailCodeRequest necr) {
        String email = necr.getEmail();
        int rand = (int) (Math.random() * 100000000);
        String code = String.format("%08d", rand);

        Verification verification = new Verification();
        verification.setCode(code);
        verification.setEmail(email);
        verification.setExpiredAt(LocalDateTime.now().plusHours(1));
        verificationMapper.insertOne(verification);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[airbnb] 이메일 인증을 완료해주세요");
        message.setText("안녕하세요!\n\n" +
                "아래 인증 코드를 입력해 인증을 완료해주세요!\n\n" +
                "인증코드 : " + code + "\n\n" +
                "감사합니다."
        );

        mailSender.send(message);
        return new EmailCodeResponse(code, verification.getExpiredAt());
    }
}
