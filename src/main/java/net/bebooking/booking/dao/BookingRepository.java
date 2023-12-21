package net.bebooking.booking.dao;


import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.tenant.model.TenantId;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository {
    Iterable<BookingId> insertAll(TenantId tenantId, Iterable<Booking> bookings);

    Booking fetchById(TenantId tenantId, BookingId bookingId);

    Iterable<Booking> fetchAll(TenantId tenantId);

    Iterable<Booking> fetchAllByIds(TenantId tenantId, Iterable<BookingId> bookingIds);

    void deleteAllByIds(TenantId tenantId, Iterable<BookingId> bookingIds);

}
