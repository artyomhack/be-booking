package net.bebooking.booking.dto;


import lombok.AllArgsConstructor;
import net.bebooking.booking.model.BookingStatus;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;

public interface BookingList extends Iterable<BookingList.Item>{

    static BookingList of(Item ...bookings) {
        return of(Arrays.asList(bookings));
    }

    static BookingList of(Iterable<Item> bookings) {
        return new BookingList() {
            @NotNull
            @Override
            public Iterator<Item> iterator() {
                return bookings.iterator();
            }
        };
    }

    @AllArgsConstructor
    class Item {
        final LocalDateTime from;
        final LocalDateTime to;
        final BookingStatus status;
        final String note;
    }
}
