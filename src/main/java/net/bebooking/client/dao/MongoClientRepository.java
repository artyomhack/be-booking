package net.bebooking.client.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import net.bebooking.client.model.Client;
import net.bebooking.client.model.ClientId;
import net.bebooking.tenant.model.TenantId;
import org.bson.Document;
import org.ecom24.common.types.ValueTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.stream.StreamSupport;

@Repository
public class MongoClientRepository implements ClientRepository {

    private final MongoClient mongoClient;

    public MongoClientRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Iterable<ClientId> insertAll(TenantId tenantId, Iterable<Client> clients) {
        //ValueTypeUtils.requireNotEmpty(tenantId);
        return StreamSupport.stream(clients.spliterator(), false)
                .map(it -> {

                    ValueTypeUtils.requireEmpty(it.getId());
                    var doc = new Document();
                    var id = ClientId.generate();
                    doc.put("_id", id);
                    doc.put("fullName", it.getFullName());
                    doc.put("email", it.getEmail());
                    doc.put("phoneNumber", it.getPhoneNumber());
                    doc.put("description", it.getDescription());
                    doc.put("country", it.getCountry());
                    doc.put("city", it.getCity());
                    doc.put("createdAt", it.getCreatedAt());
                    mongoClient.getDatabase(tenantId.toString()).getCollection("net/net.booking/client").insertOne(doc);
                    return id;
                }).toList();
    }

    @Override
    public Client fetchById(TenantId tenantId, ClientId clientId) {
        //ValueTypeUtils.requireNotEmpty(tenantId);
        ValueTypeUtils.requireNotEmpty(clientId);
        var collection = mongoClient.getDatabase(tenantId.toString()).getCollection("net/net.booking/client", Client.class);
        return collection.find(
                Filters.eq("_id", clientId.getValue()), Client.class
        ).first();
    }

    @Override
    public Iterable<Client> fetchAll(TenantId tenantId) {
        //ValueTypeUtils.requireNotEmpty(tenantId);
        var collection = mongoClient.getDatabase(tenantId.toString()).getCollection("net/net.booking/client", Client.class);
        var clients = new ArrayList<Client>();
        collection.find().into(clients);
        return clients;
    }

    @Override
    public void deleteAll(TenantId tenantId, Iterable<ClientId> clientIds) {
        //ValueTypeUtils.requireNotEmpty(tenantId);
        StreamSupport.stream(clientIds.spliterator(), false).forEach(ValueTypeUtils::requireNotEmpty);
        var collection = mongoClient.getDatabase(tenantId.toString()).getCollection("net/net.booking/client", Client.class);
        StreamSupport.stream(clientIds.spliterator(), false)
                .forEach(it -> collection.findOneAndDelete(
                        Filters.eq("_id", it.getValue()))
                );
    }
}
