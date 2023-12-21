package net.bebooking.company.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.ecom24.common.utils.StringPatterns;

@AllArgsConstructor
public class UpdateDetailsCompanyRequest {
    @NotNull
    @Pattern(regexp = "[\\wа-яА-Я]{1,250}")
    private final String companyName;
    @Pattern(regexp = StringPatterns.Email)
    private final String email;
    @NotNull
    @Pattern(regexp = StringPatterns.PhoneNumber)
    private final String phone;
    private final String address;
    private final String country;
    private final String city;
}
