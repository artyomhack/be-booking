package net.bebooking.user.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateDetailsUserRequest {
    final String fullName;
    final String email;
    final String phone;
    final String hashPassword;
}
