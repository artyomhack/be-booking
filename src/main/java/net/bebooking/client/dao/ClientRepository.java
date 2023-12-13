package net.bebooking.client.dao;


import net.bebooking.client.model.Client;
import net.bebooking.client.model.ClientId;
import net.bebooking.tenant.model.TenantId;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository {

    Iterable<ClientId> insertAll(TenantId tenantId, Iterable<Client> clients);

    Client fetchById(TenantId tenantId, ClientId clientId);

    Iterable<Client> fetchAll(TenantId tenantId);

    void deleteAll(TenantId tenantId, Iterable<ClientId> clientIds);

}
