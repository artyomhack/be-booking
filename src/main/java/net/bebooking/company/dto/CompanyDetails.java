package net.bebooking.company.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyDetails {
    private final String companyName;
    private final String email;
    private final String phone;
    private final String address;
    private final String country;
    private final String city;
}
