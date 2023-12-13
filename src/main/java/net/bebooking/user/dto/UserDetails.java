package net.bebooking.user.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetails {
    final String fullName;
    final String email;
    final String phoneNumber;
    final String hashPassword;
}
