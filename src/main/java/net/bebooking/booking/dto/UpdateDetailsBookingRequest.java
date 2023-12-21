package net.bebooking.booking.dto;

import lombok.AllArgsConstructor;
import net.bebooking.booking.model.BookingStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
public class UpdateDetailsBookingRequest {
    final LocalDateTime from;
    final LocalDateTime to;
    final BookingStatus status;
    final String note;
}
