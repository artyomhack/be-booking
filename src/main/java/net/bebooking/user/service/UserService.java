package net.bebooking.user.service;

import net.bebooking.principal.model.Principal;
import net.bebooking.user.dto.CreateUserRequest;
import net.bebooking.user.dto.ListUserDetails;
import net.bebooking.user.dto.UpdateDetailsUserRequest;
import net.bebooking.user.dto.UserDetails;
import net.bebooking.user.model.UserId;

public interface UserService {
    UserId create(Principal principal, CreateUserRequest req);

    void update(Principal principal, UpdateDetailsUserRequest req);

    UserDetails getById(Principal principal, UserId id);

    ListUserDetails getAll(Principal principal);
}
