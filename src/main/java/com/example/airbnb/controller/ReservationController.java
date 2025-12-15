package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.Reservation;
import com.example.airbnb.domain.entity.ReservationDate;
import com.example.airbnb.domain.model.ReservationDateParam;
import com.example.airbnb.domain.model.ReservationUpdateParam;
import com.example.airbnb.dto.request.EditReservationRequest;
import com.example.airbnb.dto.request.NewMessageRequest;
import com.example.airbnb.dto.request.NewReservationRequest;
import com.example.airbnb.dto.response.DeleteMessageResponse;
import com.example.airbnb.dto.response.ReservationResponse;
import com.example.airbnb.dto.response.SendMessageResponse;
import com.example.airbnb.mappers.MessageMapper;
import com.example.airbnb.mappers.ReservationMapper;
import com.example.airbnb.util.ApiUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@CrossOrigin
public class ReservationController {
    final ReservationMapper reservationMapper;
    final MessageMapper messageMapper;
    final ApiUtil apiUtil;

    // 예약 정보 1건 조회
    @GetMapping("/{code}")
    public ReservationResponse getReservation(@PathVariable String code) {
        ReservationResponse resp = new ReservationResponse();
        resp.setSuccess(false);

        Reservation reservation = reservationMapper.selectOne(code);
        if (reservation == null) {
            resp.setMessage("Reservation not found");
        } else {
            resp.setSuccess(true);
            resp.setMessage("Reservation found");
            resp.setData(reservation);
        }

        return resp;
    }

    // 예약 정보 등록
    @PostMapping
    @Transactional
    public ReservationResponse createReservation(@RequestBody @Valid NewReservationRequest nrr, BindingResult bindingResult,
                                                 @RequestAttribute String tokenId) {
        ReservationResponse resp = new ReservationResponse();
        resp.setSuccess(false);

        if (!tokenId.equals(nrr.getAccountId())) {
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

        if (reservationMapper.countDuplicateDate(nrr.toParam()) > 0) {
            resp.setMessage("ReservationDate Duplicated");
            return resp;
        }

        try {
            String code = UUID.randomUUID().toString().split("-")[0].toUpperCase();
            Reservation reservation = nrr.toReservation(code);
            int r = reservationMapper.insertOne(reservation);
            if (r != 1) {
                throw new RuntimeException("Error in insert reservation");
            }

            for (LocalDate d = nrr.getStartDate(); d.isBefore(nrr.getEndDate()) || d.equals(nrr.getEndDate()); d = d.plusDays(1)) {
                r = reservationMapper.insertReservationDate(new ReservationDate(nrr.getAccommodationId(), d));
                if (r != 1) {
                    throw new RuntimeException("Error in insert reservationDate");
                }
            }
            resp.setSuccess(true);
            resp.setMessage("Reservation created");
            resp.setData(reservation);
        } catch (Exception e) {
            resp.setMessage(e.getMessage());
            throw e;
        }

        return resp;
    }

    // 예약 정보 수정
    @PutMapping("/{code}")
    @Transactional
    public ReservationResponse updateReservation(@PathVariable String code,
                                                 @RequestBody @Valid EditReservationRequest err, BindingResult bindingResult,
                                                 @RequestAttribute String tokenId) {
        ReservationResponse resp = new ReservationResponse();
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

        try {
            Reservation reservation = reservationMapper.selectOne(code);
            if (reservation == null) {
                resp.setMessage("Reservation not found");
                return resp;
            }

            if (!tokenId.equals(reservation.getAccountId())) {
                resp.setMessage("invalid token");
                return resp;
            }

            reservationMapper.deleteReservationDate(reservation.toParam());

            if (reservationMapper.countDuplicateDate(err.toDateParam(reservation.getAccommodationId())) > 0) {
                resp.setMessage("ReservationDate Duplicated");
                return resp;
            }

            if (reservationMapper.updateReservation(err.toUpdateParam(code)) != 1) {
                throw new RuntimeException("Error in insert reservation");
            }

            for (LocalDate d = err.getStartDate(); d.isBefore(err.getEndDate()) || d.equals(err.getEndDate()); d = d.plusDays(1)) {
                if (reservationMapper.insertReservationDate(new ReservationDate(reservation.getAccommodationId(), d)) != 1) {
                    throw new RuntimeException("Error in insert reservationDate");
                }
            }
            resp.setSuccess(true);
            resp.setMessage("Reservation Update");
        } catch (Exception e) {
            resp.setMessage(e.getMessage());
            throw e;
        }
        return resp;
    }

    // 예약 정보 삭제
    @DeleteMapping("/{code}")
    @Transactional
    public ReservationResponse deleteReservation(@PathVariable String code,
                                                 @RequestAttribute String tokenId) {
        ReservationResponse resp = new ReservationResponse();
        resp.setSuccess(false);

        try {
            Reservation target = reservationMapper.selectOne(code);
            if (target == null) {
                resp.setMessage("Reservation not found");
                return resp;
            }

            if (!tokenId.equals(target.getAccountId())) {
                resp.setMessage("invalid token");
                return resp;
            }

            ReservationDateParam param = new ReservationDateParam();
            param.setAccommodationId(target.getAccommodationId());
            param.setStartDate(target.getStartDate());
            param.setEndDate(target.getEndDate());

            reservationMapper.deleteReservationByCode(code);
            reservationMapper.deleteReservationDate(param);

            resp.setSuccess(true);
            resp.setMessage("Reservation Delete Complete");
        } catch (Exception e) {
            resp.setMessage(e.getMessage());
            throw e;
        }

        return resp;
    }

    // 메시지 전송
    @PostMapping("/messages")
    public SendMessageResponse sendMessage(@RequestBody @Valid NewMessageRequest nmr, BindingResult bindingResult,
                                           @RequestAttribute String tokenId) {
        SendMessageResponse resp = new SendMessageResponse();
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

        if (!tokenId.equals(nmr.getWriterId())) {
            resp.setMessage("invalid token");
            return resp;
        }

        int r = messageMapper.insertOne(nmr.toMessage());
        if (r != 1) {
            resp.setMessage("Error in insert message");
        } else {
            resp.setSuccess(true);
            resp.setMessage("Message Insert Complete");
            resp.setMessageData(nmr.toMessage());
        }

        return resp;
    }

    @GetMapping("/test/{date}")
    public void test(@PathVariable LocalDate date) {
        System.out.println(apiUtil.holidayCheck(date));
    }

}
