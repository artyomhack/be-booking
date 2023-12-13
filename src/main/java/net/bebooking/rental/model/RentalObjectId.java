package net.bebooking.rental.model;

import org.ecom24.common.types.common.IntType;
import org.ecom24.common.utils.NumberUtils;

import java.util.Optional;

public class RentalObjectId extends IntType {

    public static RentalObjectId EMPTY = new RentalObjectId(null);

    public static RentalObjectId parse(Object intId) {
        return Optional.ofNullable(intId)
                .map(RentalObjectId::new)
                .orElse(EMPTY);
    }

    public static RentalObjectId parseNotEmpty(Object intId) {
        return Optional.ofNullable(intId)
                .map(RentalObjectId::new)
                .orElseThrow(() -> new IllegalArgumentException("Rental object id must not be empty"));
    }

    protected RentalObjectId(Object value) {
        super(value);
        NumberUtils.requireRangeOrNull(getValueOrNull(), 1, Integer.MAX_VALUE);
    }
}
