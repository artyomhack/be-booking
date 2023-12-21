package net.bebooking.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.bebooking.tenant.model.TenantId;
import org.bson.Document;

import java.util.stream.StreamSupport;

public class MongoUtils {

    public static MongoCollection<Document> requireCollection(MongoDatabase mongoDatabase, String collectionName) {
        var isExist = StreamSupport.stream(mongoDatabase.listCollectionNames().spliterator(), false)
                .anyMatch(it -> it.equals(collectionName));

        if (!isExist) mongoDatabase.createCollection(collectionName);

        return mongoDatabase.getCollection(collectionName);
    }

    public static void dropData(MongoClient mongoClient, TenantId tenantId) {
        mongoClient.getDatabase(tenantId.getValue().toString()).drop();
    }

    public static TenantId tenantTestOf() {
        return TenantId.parse("00000000-0000-0000-0000-000000000001");
    }

    public static void cleanCache(MongoCollection<?> collection) {
        collection.deleteMany(new Document());
    }
}
