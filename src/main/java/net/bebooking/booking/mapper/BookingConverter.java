package net.bebooking.booking.mapper;

import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.booking.model.BookingStatus;
import org.bson.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BookingConverter {
    public Document convert(Booking booking) {
        var id = Objects.requireNonNullElse(booking.getId().getValueOrNull(), BookingId.generate().getValue());
        return new Document()
                .append("_id", id)
                .append("from", booking.getFrom())
                .append("to", booking.getTo())
                .append("status", booking.getStatus().toString())
                .append("note", booking.getNote())
                .append("createdAt", booking.getCreatedAt());
    }

    public Booking convert(Document document) {
        return Booking.of(
                BookingId.parseNotEmpty(document.get("_id")),
                document.getDate("from").toInstant(),
                document.getDate("to").toInstant(),
                BookingStatus.valueOf(document.getString("status")),
                document.getString("note"),
                document.getDate("createdAt")
                                 .toInstant()
                                 .atZone(ZoneId.systemDefault())
                                 .toLocalDateTime()
        );
    }
}
