package net.bebooking.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import net.bebooking.booking.model.BookingStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
public class BookingDetails {
    final LocalDateTime from;
    final LocalDateTime to;
    @NotNull
    final BookingStatus status;
    final String note;
}
