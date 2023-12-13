package net.bebooking.rental.service;


import net.bebooking.principal.model.Principal;
import net.bebooking.rental.dto.CreateRentalObjectRequest;
import net.bebooking.rental.dto.RentalObjectDetails;
import net.bebooking.rental.dto.RentalObjectList;
import net.bebooking.rental.dto.UpdateDetailsRentalObjectRequest;
import net.bebooking.rental.model.RentalObjectId;

public interface RentalObjectService {
    RentalObjectId createRentalObject(Principal principal, CreateRentalObjectRequest req); //ID

    void updateDetails(Principal principal, UpdateDetailsRentalObjectRequest req);

    RentalObjectDetails getById(Principal principal, RentalObjectId id);

    RentalObjectList getAll(Principal principal);
}
