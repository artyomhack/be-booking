package net.bebooking.principal.model;

import net.bebooking.tenant.model.TenantId;
import net.bebooking.user.model.UserId;


public record Principal(UserId userId, TenantId tenantId) {

}
