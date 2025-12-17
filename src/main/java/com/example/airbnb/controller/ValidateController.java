package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.Accommodation;
import com.example.airbnb.domain.entity.Reservation;
import com.example.airbnb.domain.entity.Verification;
import com.example.airbnb.dto.request.account.NewEmailCodeRequest;
import com.example.airbnb.dto.request.reservations.CheckReservationRequest;
import com.example.airbnb.dto.response.CheckReservationResponse;
import com.example.airbnb.dto.response.DuplicateResponse;
import com.example.airbnb.dto.response.EmailCodeResponse;
import com.example.airbnb.mappers.AccommodationMapper;
import com.example.airbnb.mappers.AccountMapper;
import com.example.airbnb.mappers.ReservationMapper;
import com.example.airbnb.mappers.VerificationMapper;
import com.example.airbnb.util.ApiUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
@CrossOrigin
public class ValidateController {
    final AccountMapper accountMapper;
    final VerificationMapper verificationMapper;
    final JavaMailSender mailSender;
    final ReservationMapper reservationMapper;
    private final ApiUtil apiUtil;
    final AccommodationMapper accommodationMapper;

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

    @GetMapping("/reservation")
    public CheckReservationResponse checkReservationAvailable(@ModelAttribute @Valid CheckReservationRequest crr, BindingResult bindingResult) {
        CheckReservationResponse resp = new CheckReservationResponse();
        resp.setSuccess(false);
        resp.setReservationAvailable(false);

        if (bindingResult.hasErrors()) {
            FieldError fe = bindingResult.getFieldError();
            if (fe != null) {
                resp.setMessage(fe.getField() + " 필드 오류: " + fe.getDefaultMessage());
            } else {
                resp.setMessage("요청 값 오류: " + bindingResult.getAllErrors().getFirst().getDefaultMessage());
            }
            return resp;
        }

        Accommodation accommodation = accommodationMapper.selectAccommodationById(crr.getAccommodationId());
        if(accommodation==null){
            resp.setMessage("accommodation Not Found");
            return resp;
        }


        resp.setSuccess(true);
        if (reservationMapper.countDuplicateDate(crr.toParam()) > 0) {
            resp.setMessage("ReservationDate Duplicated");
            return resp;
        }

        int totalPrice = 0;
        for (LocalDate date = crr.getStartDate(); date.isBefore(crr.getEndDate()); date = date.plusDays(1)) {
            if(apiUtil.holidayCheck(date)){
                totalPrice += (int)(accommodation.getPrice() * (accommodation.getExtraRate() + 1));
            }else{
                totalPrice += accommodation.getPrice();
            }
        }


        resp.setReservationAvailable(true);
        resp.setMessage("Reservation Available");
        resp.setTotalPrice(totalPrice);

        return resp;
    }
}
