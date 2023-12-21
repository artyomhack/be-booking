package net.bebooking.utils;


import net.bebooking.tenant.model.TenantId;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class SqlUtils {
    public static String insert(TenantId tenantId, String tableName, String... field) {
        var fieldList = Arrays.asList(field);
        var params = String.join(",", fieldList);
        var values = fieldList.stream().map(it -> ":" + it).collect(Collectors.joining(","));
        return "INSERT INTO " + tenantId + "." + tableName +
                " (" + params + ") " +
                "VALUES (" + values + ");";
    }

    public static String update(TenantId tenantId, String nameId, String tableName, String ...fields) {
        var fieldList = Arrays.asList(fields);
        var values = fieldList.stream().map(it-> it + "=:" + it).collect(Collectors.joining(", "));
        return "UPDATE " + tenantId + "." + tableName +
                " SET " + values +
                " WHERE " + nameId + " = id";
    }

    public static String getByColumn(TenantId tenantId, String tableName, String column) {
        return "SELECT * FROM " + tenantId + "." + tableName +
                " WHERE " + column + "= ?";
    }

    public static String getAll(TenantId tenantId, String tableName) {
        return "SELECT * FROM " + tenantId + "." + tableName;
    }
}
