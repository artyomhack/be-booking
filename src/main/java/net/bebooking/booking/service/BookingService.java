package net.bebooking.booking.service;


import net.bebooking.booking.dto.CreateBookingRequest;
import net.bebooking.booking.model.BookingId;
import net.bebooking.principal.model.Principal;

import java.time.LocalDateTime;

public interface BookingService {
    // 10.11.2010T11.00 - 20.11.2010T12.00 BookTime
    // 10.11.2010T15.35 - 19.11.2010T20.15 BookTime
    BookingId createBooking(Principal principal, CreateBookingRequest req); //ID

    default void checkIn(Principal principal, BookingId bokingId, LocalDateTime date) {
//        book = repo.findById();
//        book.set();
//        book.setStatus("");
//        repo.update(book);
    }

}
