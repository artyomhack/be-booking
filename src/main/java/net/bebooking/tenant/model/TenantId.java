package net.bebooking.tenant.model;

import org.ecom24.common.types.common.UuidType;
import org.ecom24.common.utils.StringUtils;

import java.util.Optional;

final public class TenantId extends UuidType {

    public static TenantId EMPTY = new TenantId(null);

    public static TenantId generate() {
        var uuid = StringUtils.generateUuid(TenantId.class.getSimpleName(), 1);
        return parseNotEmpty(uuid);
    }

    public static TenantId parse(Object uuid) {
        return Optional.of(uuid)
                .map(TenantId::new)
                .orElse(EMPTY);
    }

    public static TenantId parseNotEmpty(Object uuid) {
        return Optional.of(uuid)
                .map(TenantId::new)
                .orElseThrow(() -> new IllegalArgumentException("Tenant id must not be empty"));
    }

    private TenantId(Object value) {
        super(value);
    }
}