package net.bebooking.client.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.ecom24.common.utils.StringPatterns;

@AllArgsConstructor
public class CreateClientRequest {

    @NotNull
    @Pattern(regexp = "[\\wа-яА-Я]{3,150}")
    public final String fullName;

    @Pattern(regexp = StringPatterns.Email)
    public final String email;

    @NotNull
    @Pattern(regexp = StringPatterns.PhoneNumber)
    public final String phone;
}
