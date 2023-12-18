package net.bebooking.client.model;

import org.ecom24.common.types.common.UuidType;
import org.ecom24.common.utils.StringUtils;

import java.util.Optional;

public class ClientId extends UuidType {

    public static ClientId EMPTY = new ClientId(null);

    public static ClientId generate() {
        var uuid = StringUtils.generateUuid(ClientId.class.getName(), 1);
        return parseNotEmpty(uuid);
    }

    public static ClientId parse(Object uuid) {
        return Optional.ofNullable(uuid)
                .map(ClientId::new)
                .orElse(EMPTY);
    }

    public static ClientId parseNotEmpty(Object uuid) {
        return Optional.ofNullable(uuid)
                .map(ClientId::new)
                .orElseThrow(() -> new IllegalArgumentException("Client id must not be empty"));
    }

    protected ClientId(Object value) {
        super(value);
    }
}
