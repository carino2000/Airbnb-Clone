package com.example.airbnb.dto.request.reservations;

import com.example.airbnb.domain.entity.Message;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewMessageRequest {
    @NotBlank
    String reservationCode;
    @NotBlank
    String writerId;
    @NotBlank
    String recipientId;
    @NotBlank
    String content;

    public Message toMessage() {
        Message message = new Message();
        message.setReservationCode(this.reservationCode);
        message.setWriterId(this.writerId);
        message.setRecipientId(this.recipientId);
        message.setContent(this.content);
        message.setReadFlag(false);
        return message;
    }
}
