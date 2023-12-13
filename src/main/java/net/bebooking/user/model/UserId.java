package net.bebooking.user.model;

import org.ecom24.common.types.common.IntType;
import org.ecom24.common.utils.NumberUtils;

import java.util.Optional;

public class UserId extends IntType {

    public final static UserId EMPTY = new UserId(null);

    public static UserId parse(Object intId) {
        return Optional.ofNullable(intId)
                .map(UserId::new)
                .orElse(EMPTY);
    }

    public static UserId parseNotEmpty(Object intId) {
        return Optional.ofNullable(intId)
                .map(UserId::new)
                .orElseThrow(() -> new IllegalArgumentException("User id must not be null"));
    }

    protected UserId(Object value) {
        super(value);
        NumberUtils.requireRangeOrNull(getValueOrNull(), 1, Integer.MAX_VALUE);
    }
}
