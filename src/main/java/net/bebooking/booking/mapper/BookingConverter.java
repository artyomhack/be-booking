package net.bebooking.booking.mapper;

import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.booking.model.BookingStatus;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class BookingConverter {
    public static Document convert(Booking booking) {
        return new Document()
                .append("_id", Optional.ofNullable(booking.getId())
                                       .orElseGet(BookingId::generate))
                .append("from", booking.getFrom())
                .append("to", booking.getTo())
                .append("status", booking.getStatus().toString())
                .append("note", booking.getNote())
                .append("createdAt", booking.getCreatedAt());
    }

    public static Booking convert(Document document) {
        return Booking.of(
                BookingId.parseNotEmpty(document.getObjectId("_id")),
                document.get("from", LocalDateTime.class),
                document.get("to", LocalDateTime.class),
                BookingStatus.valueOf(document.getString("status")),
                document.getString("note"),
                document.get("createdAt", LocalDateTime.class)
        );
    }
}
