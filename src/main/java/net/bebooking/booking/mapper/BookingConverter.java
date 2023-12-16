package net.bebooking.booking.mapper;

import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.booking.model.BookingStatus;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BookingConverter {
    public Document convert(Booking booking) {
        return new Document()
                .append("_id", booking.getId())
                .append("from", booking.getFrom().atZone(ZoneOffset.UTC))
                .append("to", booking.getTo().atZone(ZoneOffset.UTC))
                .append("status", booking.getStatus().toString())
                .append("note", booking.getNote())
                .append("createdAt", booking.getCreatedAt().atZone(ZoneOffset.UTC));
    }

    public Booking convert(Document document) {
        return Booking.of(
                BookingId.parseNotEmpty(document.get("_id")),
                LocalDateTime.ofInstant(document.getDate("from").toInstant(), ZoneOffset.UTC),
                LocalDateTime.ofInstant(document.getDate("to").toInstant(), ZoneOffset.UTC),
                BookingStatus.valueOf(document.getString("status")),
                document.getString("note"),
                LocalDateTime.ofInstant(document.getDate("createdAt").toInstant(), ZoneOffset.UTC)
        );
    }
}
