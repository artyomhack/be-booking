package net.bebooking.user.model;

import org.ecom24.common.types.common.UuidType;
import org.ecom24.common.utils.StringUtils;

import java.util.Optional;

public class UserId extends UuidType {

    public final static UserId EMPTY = new UserId(null);

    public static UserId generate() {
        var uuid = StringUtils.generateUuid(UserId.class.getSimpleName(), 1);
        return parseNotEmpty(uuid);
    }

    public static UserId parse(Object uuid) {
        return Optional.ofNullable(uuid)
                .map(UserId::new)
                .orElse(EMPTY);
    }

    public static UserId parseNotEmpty(Object uuid) {
        return Optional.ofNullable(uuid)
                .map(UserId::new)
                .orElseThrow(() -> new IllegalArgumentException("User id must not be null"));
    }

    protected UserId(Object value) {
        super(value);
    }
}
