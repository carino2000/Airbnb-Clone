package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.Reservation;
import com.example.airbnb.domain.entity.ReservationDate;
import com.example.airbnb.domain.model.ReservationDateParam;
import com.example.airbnb.domain.model.ReservationUpdateParam;
import com.example.airbnb.dto.request.EditReservationRequest;
import com.example.airbnb.dto.request.NewReservationRequest;
import com.example.airbnb.dto.response.ReservationResponse;
import com.example.airbnb.mappers.ReservationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@CrossOrigin
public class ReservationController {
    final ReservationMapper reservationMapper;

    @GetMapping("/{code}")
    public ReservationResponse getReservations(@PathVariable String code) {
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

    @PostMapping
    @Transactional
    public ReservationResponse createReservation(@RequestBody NewReservationRequest nrr) {
        ReservationResponse resp = new ReservationResponse();
        resp.setSuccess(false);

        if(reservationMapper.countDuplicateDate(nrr.toParam()) > 0){
            resp.setMessage("ReservationDate Duplicated");
            return resp;
        }

        try{
            String code = UUID.randomUUID().toString().split("-")[0].toUpperCase();
            Reservation reservation = nrr.toReservation(code);
            int r = reservationMapper.insertOne(reservation);
            if (r != 1) {
                throw new RuntimeException("Error in insert reservation");
            }

            List<ReservationDate> list = new ArrayList<>();
            for (LocalDate d = nrr.getStartDate(); d.isBefore(nrr.getEndDate()) || d.equals(nrr.getEndDate()); d = d.plusDays(1)) {
                r = reservationMapper.insertReservationDate(new ReservationDate(nrr.getAccommodationId(), d));
                if (r != 1) {
                    throw new RuntimeException("Error in insert reservationDate");
                }
            }
            resp.setSuccess(true);
            resp.setMessage("Reservation created");
        }catch (Exception e){
            resp.setMessage(e.getMessage());
            throw e;
        }

        return resp;
    }

    @PutMapping("/{code}")
    @Transactional
    public ReservationResponse updateReservation(@PathVariable String code,
                                                 @RequestBody @Valid EditReservationRequest err,
                                                                        BindingResult bindingResult) {
        ReservationResponse resp = new ReservationResponse();
        resp.setSuccess(false);

        if (bindingResult.hasErrors()) {
            FieldError fe = bindingResult.getFieldError();
            if (fe != null) {
                resp.setMessage(fe.getField() +" 필드의 Valid 오류 : " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            }else{
                resp.setMessage("unexpected error : " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            System.out.println("Error in editPassword: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return resp;
        }

        Reservation reservation = reservationMapper.selectOne(code);
        if(reservation == null){
            resp.setMessage("Reservation not found");
            return resp;
        }

        if(reservationMapper.countDuplicateDate(reservation.toParam()) > 0){
            resp.setMessage("ReservationDate Duplicated");
            return resp;
        }

        try{
            reservationMapper.deleteReservationDate(reservation.toParam());
            ReservationUpdateParam param = new ReservationUpdateParam();

            param.setCode(code);
            param.setVisitors(err.getVisitors());
            param.setStartDate(err.getStartDate());
            param.setEndDate(err.getEndDate());
            param.setPrice(err.getPrice());



        }catch (Exception e){
            resp.setMessage(e.getMessage());
            throw e;
        }


        return resp;
    }
}
