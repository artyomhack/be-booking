package net.bebooking.client.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ecom24.common.utils.StringPatterns;

@Getter
@AllArgsConstructor
public class UpdateDetailsClientRequest {
    @NotNull
    @Pattern(regexp = "[\\wа-яА-Я]{3,150}")
    private final String fullName;
    @Pattern(regexp = StringPatterns.Email)
    private final String email;
    @NotNull
    @Pattern(regexp = StringPatterns.PhoneNumber)
    private final String phone;
    private final String description;
    private final String country;
    private final String city;
}
