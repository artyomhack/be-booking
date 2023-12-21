package net.bebooking.rental.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateDetailsRentalObjectRequest {
    final String name;
    final String rentalType;
    final String description;
    final String routeMap;
    final String address;
    final String postCode;
    final Double latitude;
    final Double longitude;
}
