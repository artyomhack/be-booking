package net.bebooking.booking.model;

import org.ecom24.common.types.common.IntType;
import org.ecom24.common.types.common.UuidType;
import org.ecom24.common.utils.NumberUtils;
import org.ecom24.common.utils.StringUtils;

import java.util.Optional;

public class BookingId extends UuidType {

    public static BookingId EMPTY = new BookingId(null);

    public static BookingId generate() {
        var uuid = StringUtils.generateUuid(BookingId.class.getSimpleName(), 1);
        return parseNotEmpty(uuid);
    }

    public static BookingId parse(Object uuid) {
        return Optional
                .ofNullable(uuid)
                .map(BookingId::new)
                .orElse(EMPTY);
    }


    public static BookingId parseNotEmpty(Object uuid) {
        return Optional
                .ofNullable(uuid)
                .map(BookingId::new)
                .orElseThrow(() -> new IllegalArgumentException("Booking id must not be null"));

    }

    protected BookingId(Object value) {
        super(value);
    }




}
