package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.Reservation;
import com.example.airbnb.domain.entity.ReservationDate;
import com.example.airbnb.dto.request.NewReservationRequest;
import com.example.airbnb.dto.response.ReservationResponse;
import com.example.airbnb.mappers.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
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

        System.out.println(nrr.toString());

        try{
            String code = UUID.randomUUID().toString().split("-")[0].toUpperCase();
            Reservation reservation = nrr.toReservation(code);
            int r = reservationMapper.insertOne(reservation);
            if (r != 1) {
                throw new RuntimeException("Error in creating reservation");
            }

            List<ReservationDate> list = new ArrayList<>();
            for (LocalDate d = nrr.getStartDate(); d.isBefore(nrr.getEndDate()) || d.equals(nrr.getEndDate()); d = d.plusDays(1)) {
                r = reservationMapper.insertReservationDate(new ReservationDate(nrr.getAccommodationId(), d));
                if (r != 1) {
                    throw new RuntimeException("Error in creating reservation");
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
}
