package com.example.airbnb.controller;

import com.example.airbnb.domain.entity.*;
import com.example.airbnb.domain.model.ReservationDateParam;
import com.example.airbnb.dto.projection.MessageRoom;
import com.example.airbnb.dto.request.reservations.EditReservationRequest;
import com.example.airbnb.dto.request.reservations.NewMessageRequest;
import com.example.airbnb.dto.request.reservations.NewReservationRequest;
import com.example.airbnb.dto.request.reservations.ReviewRequest;
import com.example.airbnb.dto.response.reservations.*;
import com.example.airbnb.mappers.AccommodationMapper;
import com.example.airbnb.mappers.MessageMapper;
import com.example.airbnb.mappers.ReservationMapper;
import com.example.airbnb.mappers.ReviewMapper;
import com.example.airbnb.util.ApiUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@CrossOrigin
public class ReservationController {
    final ReservationMapper reservationMapper;
    final MessageMapper messageMapper;
    final ApiUtil apiUtil;
    final ReviewMapper reviewMapper;
    private final AccommodationMapper accommodationMapper;

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
    public ReservationResponse createReservation(@RequestBody @Valid NewReservationRequest nrr, BindingResult bindingResult, @RequestAttribute String tokenId) {
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
    public ReservationResponse updateReservation(@PathVariable String code, @RequestBody @Valid EditReservationRequest err, BindingResult bindingResult, @RequestAttribute String tokenId) {
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
    public ReservationResponse deleteReservation(@PathVariable String code, @RequestAttribute String tokenId) {
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
    public SendMessageResponse sendMessage(@RequestBody @Valid NewMessageRequest nmr,
                                           BindingResult bindingResult,
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

        int r = messageMapper.insertMessage(nmr.toMessage());
        if (r != 1) {
            resp.setMessage("Error in insert message");
        } else {
            resp.setSuccess(true);
            resp.setMessage("Message Insert Complete");
            resp.setMessageData(nmr.toMessage());
        }

        return resp;
    }

    //카톡방? 리스트 가져오기
    @GetMapping("/messages/list/{accountId}")
    public SelectMessageListResponse selectMessageList(
            @PathVariable String accountId,
            @RequestAttribute String tokenId) {

        SelectMessageListResponse resp = new SelectMessageListResponse();

        if (!tokenId.equals(accountId)) {
            resp.setMessage("invalid token");
            return resp;
        }

        List<MessageRoom> messageRooms = new ArrayList<>();
        Set<String> usedReservationCodes = new HashSet<>();
        Set<Integer> accommodationIds = new HashSet<>();

        /* ================== HOST 입장 ================== */
        List<Accommodation> accommodations =
                accommodationMapper.selectAccommodationByHostId(accountId);

        for (Accommodation accommodation : accommodations) {
            accommodationIds.add(accommodation.getId());

            List<Reservation> reservations =
                    reservationMapper.selectReservationByAccommodationId(accommodation.getId());

            for (Reservation reservation : reservations) {
                if (accountId.equals(reservation.getAccountId())) continue;
                if (usedReservationCodes.contains(reservation.getCode())) continue;

                usedReservationCodes.add(reservation.getCode());

                MessageRoom room = new MessageRoom();
                room.setReservationCode(reservation.getCode());
                room.setRecipientId(reservation.getAccountId());

                Message lastMsg = messageMapper.selectLastMessageByCode(reservation.getCode());
                if (lastMsg != null) {
                    room.setLastMessage(lastMsg.getContent());
                    room.setLastReceiveTime(lastMsg.getWriteAt());
                    room.setUnReadCount(
                            messageMapper.countMessageReadFlagByCode(reservation.getCode())
                    );
                } else {
                    room.setLastMessage("아직 대화가 없습니다");
                    room.setLastReceiveTime(LocalDateTime.now());
                    room.setUnReadCount(0);
                }

                messageRooms.add(room);
            }
        }

        /* ================== GUEST 입장 ================== */
        List<Reservation> myReservations =
                reservationMapper.selectReservationByAccountId(accountId);

        for (Reservation r : myReservations) {
            if (accommodationIds.contains(r.getAccommodationId())) continue;
            if (usedReservationCodes.contains(r.getCode())) continue;

            usedReservationCodes.add(r.getCode());

            Accommodation a =
                    accommodationMapper.selectAccommodationById(r.getAccommodationId());

            MessageRoom room = new MessageRoom();
            room.setReservationCode(r.getCode());
            room.setRecipientId(a.getHostId());

            Message lastMsg = messageMapper.selectLastMessageByCode(r.getCode());
            if (lastMsg != null) {
                room.setLastMessage(lastMsg.getContent());
                room.setLastReceiveTime(lastMsg.getWriteAt());
                room.setUnReadCount(
                        messageMapper.countMessageReadFlagByCode(r.getCode())
                );
            } else {
                room.setLastMessage("새로운 대화를 시작해보세요!");
                room.setLastReceiveTime(LocalDateTime.now());
                room.setUnReadCount(0);
            }

            messageRooms.add(room);
        }

        resp.setSuccess(true);
        resp.setMessage("MessageList Select Complete");
        resp.setMessageRooms(messageRooms);
        return resp;
    }


    //카톡방? 1개 대화 내역 가져오기
    @GetMapping("/message/{reservationCode}")
    public SelectMessagesResponse selectMessage(@PathVariable String reservationCode,
                                                @RequestAttribute String tokenId) {
        SelectMessagesResponse resp = new SelectMessagesResponse();
        resp.setSuccess(false);

        if (tokenId == null) {
            resp.setMessage("invalid token");
            return resp;
        }

        List<Message> messages = messageMapper.selectMessageByCode(reservationCode);
        if(messages.isEmpty()){
            resp.setMessage("message not found");
            return resp;
        }else{
            for(Message m : messages){
                messageMapper.updateMessageReadFlagById(m.getId());
            }
            resp.setSuccess(true);
            resp.setMessage("Message Select Complete");
            resp.setMessages(messages);
        }

        return resp;
    }

    // 쪽지 삭제
    @DeleteMapping("/messages/{messageId}")
    public DeleteMessageResponse deleteMessage(@PathVariable int messageId) {

        int massage = messageMapper.deleteMessage(messageId);

        return DeleteMessageResponse.builder()
                .massageDelete(massage)
                .success(true)
                .build();
    }

    // 쪽지 조회
    @GetMapping("/messages/{recipientId}")
    public SelectMessagesResponse selectMessage(@PathVariable String recipientId) {

        List<Message> messages = messageMapper.selectMessage(recipientId);


        return SelectMessagesResponse.builder()
                .messages(messages)
                .success(true)
                .build();
    }

    // 리뷰 작성
    @PostMapping("/{reservationCode}/reviews")
    public WriteReviewResponse createReview(@PathVariable String reservationCode,
                                            @RequestBody ReviewRequest reviewRequest,
                                            @RequestAttribute String tokenId) {

        Review review = new Review();
        review.setAccommodationId(reviewRequest.getAccommodationId());
        review.setReservationCode(reservationCode);
        review.setRating(reviewRequest.getRating());
        review.setContent(reviewRequest.getContent());
        review.setAccountId(reviewRequest.getAccountId());

        reviewMapper.insertReview(review);

        return WriteReviewResponse.builder()
                .review(review)
                .success(true)
                .build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}/reviews")
    public DeleteReviewResponse deleteReview(@PathVariable int reviewId) {

        int review = reviewMapper.deleteReview(reviewId);


        return DeleteReviewResponse.builder()
                .review(review)
                .success(true)
                .build();
    }
}
