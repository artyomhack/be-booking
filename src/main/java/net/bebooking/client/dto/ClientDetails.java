package net.bebooking.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientDetails {
    private final String fullName;
    private final String email;
    private final String phone;
    private final String description;
    private final String country;
    private final String city;
}
