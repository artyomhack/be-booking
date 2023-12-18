package net.bebooking.client.service;



import net.bebooking.client.dto.ClientDetails;
import net.bebooking.client.dto.ClientList;
import net.bebooking.client.dto.CreateClientRequest;
import net.bebooking.client.dto.UpdateDetailsClientRequest;
import net.bebooking.client.model.ClientId;
import net.bebooking.principal.model.Principal;

public interface ClientService {
    ClientId createClient(Principal principal, CreateClientRequest request); //ID

    void updateDetails(Object principal, UpdateDetailsClientRequest req);

    ClientDetails getById(Principal principal, ClientId id);

    ClientList getAll(Principal principal);

    void setDefaultPassport(Object principal, ClientId id, Integer passportId);

}
