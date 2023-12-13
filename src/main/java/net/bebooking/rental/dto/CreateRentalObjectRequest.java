package net.bebooking.rental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class  CreateRentalObjectRequest {
    @NotNull
    @Pattern(regexp = "[\\wа-яА-Я]{1,50}")
    final String name;
    @NotNull
    final String rentalType;
    final String description;
    final String routeMap;
    final String address;
    final String postCode;
}
