package net.bebooking.booking.model;

import lombok.Getter;
import lombok.Setter;
import org.ecom24.common.utils.ErrorUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Booking {

    private final BookingId id;

    private LocalDateTime from;

    private LocalDateTime to;

    @Setter
    private BookingStatus status;

    @Setter
    private String note;

    private final LocalDateTime createdAt;

    public static Booking newOf(LocalDateTime from, LocalDateTime to, String note) {
        return new Booking(BookingId.EMPTY, from, to, BookingStatus.CREATED, note, null);
    }

    public static Booking of(Integer id, LocalDateTime from, LocalDateTime to, BookingStatus status, String note, LocalDateTime createdAt) {
        return new Booking(BookingId.parseNotEmpty(id), from, to, status, note, createdAt);
    }

    private Booking(BookingId id, LocalDateTime from, LocalDateTime to, BookingStatus status, String note, LocalDateTime createdAt) {
        Objects.requireNonNull(id);
        this.id = id;
        setFrom(from);
        setTo(to);
        this.status = status;
        this.note = note;
        this.createdAt = Objects.requireNonNullElse(createdAt, LocalDateTime.now());
    }


    public void setFrom(LocalDateTime from) {

        Objects.requireNonNull(from);

        if (Objects.nonNull(to) && from.isAfter(to))
            ErrorUtils.argumentError("Date from must be less than date to");

        this.from = from;
    }

    public void setTo(LocalDateTime to) {
        Objects.requireNonNull(to);

        if (Objects.nonNull(from) && to.isBefore(from))
            ErrorUtils.argumentError("Date to must be greater than date from");

        this.to = to;
    }

}
