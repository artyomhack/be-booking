package net.bebooking.company.model;

import org.ecom24.common.types.common.IntType;
import org.ecom24.common.utils.NumberUtils;

import java.util.Optional;

public class CompanyId extends IntType {

    public static CompanyId EMPTY = new CompanyId(null);

    public static CompanyId parse(Object intId) {
        return Optional.ofNullable(intId)
                .map(CompanyId::new)
                .orElse(EMPTY);
    }

    public static CompanyId parseNotEmpty(Object intId) {
        return Optional.ofNullable(intId)
                .map(CompanyId::new)
                .orElseThrow(() -> new IllegalArgumentException("Company id must not be empty"));
    }

    protected CompanyId(Object value) {
        super(value);
        NumberUtils.requireRangeOrNull(getValueOrNull(), 1, Integer.MAX_VALUE);
    }
}
