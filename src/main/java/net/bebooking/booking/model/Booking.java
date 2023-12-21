package net.bebooking.booking.model;

import lombok.Getter;
import lombok.Setter;
import org.ecom24.common.utils.ErrorUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Booking implements Comparable<Booking>{

    private final BookingId id;

    private Instant from;

    private Instant to;

    @Setter
    private BookingStatus status;

    @Setter
    private String note;

    private final LocalDateTime createdAt;

    public static Booking newOf(Instant from, Instant to, String note) {
        return new Booking(BookingId.EMPTY, from, to, BookingStatus.CREATED, note, null);
    }

    public static Booking of(BookingId id, Instant from, Instant to, BookingStatus status, String note, LocalDateTime createdAt) {
        return new Booking(BookingId.parseNotEmpty(id), from, to, status, note, createdAt);
    }

    private Booking(BookingId id, Instant from, Instant to, BookingStatus status, String note, LocalDateTime createdAt) {
        Objects.requireNonNull(id);
        this.id = id;
        setFrom(from);
        setTo(to);
        this.status = status;
        this.note = note;
        this.createdAt = Objects.requireNonNullElse(createdAt, LocalDateTime.now());
    }


    public void setFrom(Instant from) {

        Objects.requireNonNull(from);

        if (Objects.nonNull(to) && from.isAfter(to))
            ErrorUtils.argumentError("Date from must be less than date to");

        this.from = from;
    }

    public void setTo(Instant to) {
        Objects.requireNonNull(to);

        if (Objects.nonNull(from) && to.isBefore(from))
            ErrorUtils.argumentError("Date to must be greater than date from");

        this.to = to;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;

        return Objects.equals(this.id.getValue(), booking.id.getValue());
    }

    @Override
    public int hashCode() {
        return Booking.class.hashCode() + this.id.getClass().hashCode() * 31;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", status=" + status +
                ", note='" + note + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public int compareTo(@NotNull Booking o) {
        return this.id.getValue().compareTo(o.getId().getValue());
    }
}
