package net.bebooking.utils;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoUtils {
    public static void cleanCollection(MongoCollection collection) {
        collection.deleteMany(new Document());
    }
}
