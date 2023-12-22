package net.bebooking.client.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import net.bebooking.client.mapper.ClientConverter;
import net.bebooking.client.model.Client;
import net.bebooking.client.model.ClientId;
import net.bebooking.tenant.model.TenantId;
import net.bebooking.utils.MongoUtils;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.ecom24.common.types.AbstractType;
import org.ecom24.common.types.ValueTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Repository
public class MongoClientRepository implements ClientRepository {

    private final MongoClient mongoClient;

    private final CodecRegistry codecRegistry;

    public MongoClientRepository(MongoClient mongoClient, CodecRegistry clientCodecRegistry) {
        this.mongoClient = mongoClient;
        this.codecRegistry = clientCodecRegistry;
    }

    @Override
    public Iterable<ClientId> insertAll(TenantId tenantId, Iterable<Client> clients) {
        ValueTypeUtils.requireNotEmpty(tenantId);

        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = MongoUtils.requireCollection(data, "client");

        return StreamSupport.stream(clients.spliterator(), false).map(it -> {
            ValueTypeUtils.requireEmpty(it.getId());
            var doc = new ClientConverter().convert(it);
            collection.insertOne(doc);
            return ClientId.parseNotEmpty(doc.get("_id", UUID.class));
        }).toList();
    }

    @Override
    public Client fetchById(TenantId tenantId, ClientId clientId) {
        ValueTypeUtils.requireNotEmpty(tenantId);
        ValueTypeUtils.requireNotEmpty(clientId);

        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = MongoUtils.requireCollection(data, "client").withDocumentClass(Client.class);

        return collection.find(
                Filters.eq("_id", clientId.getValue()), Client.class
        ).first();
    }

    @Override
    public Iterable<Client> fetchAll(TenantId tenantId) {
        ValueTypeUtils.requireNotEmpty(tenantId);

        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = MongoUtils.requireCollection(data, "client").withDocumentClass(Client.class);

        var clients = new ArrayList<Client>();

        return collection.find().into(clients).stream().toList();
    }

    @Override
    public Iterable<Client> fetchAllByIds(TenantId tenantId, Iterable<ClientId> clientIds) {
        ValueTypeUtils.requireNotEmpty(tenantId);
        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = MongoUtils.requireCollection(data, "client").withDocumentClass(Client.class);

        var ids = StreamSupport.stream(clientIds.spliterator(), false)
                .map(AbstractType::getValue)
                .toList();

        var doc = new Document("_id", new Document("$in", ids));
        var clients = new ArrayList<Client>();

        return collection.find(doc).into(clients);
    }

    @Override
    public void deleteAllByIds(TenantId tenantId, Iterable<ClientId> clientIds) {
        ValueTypeUtils.requireNotEmpty(tenantId);

        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = data.getCollection("client").withDocumentClass(Client.class);

        var ids = StreamSupport.stream(clientIds.spliterator(), false)
                .map(AbstractType::getValue)
                .toList();

        var filter = new Document("_id", new Document("$in", ids));
        collection.deleteMany(filter);
    }
}
