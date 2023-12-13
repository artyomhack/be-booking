package net.bebooking.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class CreateBookingRequest {
    @NotNull
    final Integer clientId;

    @Past(message = "Date must be before")
    final LocalDateTime from;

    @Past(message = "Date must be after")
    final LocalDateTime to;

    final String note;
}
