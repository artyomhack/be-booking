package net.bebooking.user.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.ecom24.common.utils.StringPatterns;

@AllArgsConstructor
public class CreateUserRequest {
    @NotNull
    @Pattern(regexp = "[\\wа-яА-Я]{3,150}")
    final String fullName;

    @Pattern(regexp = StringPatterns.Email)
    final String email;

    @Pattern(regexp = StringPatterns.PhoneNumber)
    final String phone;

    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")
    final String hashPassword;
}
