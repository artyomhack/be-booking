package net.bebooking.booking.mapper;

import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.booking.model.BookingStatus;
import org.bson.Document;

import java.time.LocalDateTime;
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
                .append("from", mapToDate(booking.getFrom()))
                .append("to", mapToDate(booking.getTo()))
                .append("status", booking.getStatus().toString())
                .append("note", booking.getNote())
                .append("createdAt", mapToDate(booking.getCreatedAt()));
    }

    public Booking convert(Document document) {
        return Booking.of(
                BookingId.parseNotEmpty(document.get("_id")),
                mapToLocalDateTime(document, "from"),
                mapToLocalDateTime(document, "to"),
                BookingStatus.valueOf(document.getString("status")),
                document.getString("note"),
                mapToLocalDateTime(document, "createdAt")
        );
    }

    private LocalDateTime mapToLocalDateTime(Document document, String fieldName) {
        Date date = document.getDate(fieldName);
        return Optional.of(LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC))
                .orElseThrow(() -> new NullPointerException("Date can not be null") );
    }

    private Date mapToDate(LocalDateTime localDateTime) {
        var zoneDateTime = localDateTime.atZone(ZoneOffset.UTC);
        return Date.from(zoneDateTime.toInstant());
    }
}
