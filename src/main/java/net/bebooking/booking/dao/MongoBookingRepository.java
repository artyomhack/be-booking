package net.bebooking.booking.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.tenant.model.TenantId;
import org.bson.Document;
import org.ecom24.common.types.ValueTypeUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.stream.StreamSupport;

@Repository
public class MongoBookingRepository implements BookingRepository {

    private final MongoClient mongoClient;

    public MongoBookingRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Iterable<BookingId> insertAll(TenantId tenantId, Iterable<Booking> bookings) {
        //ValueTypeUtils.requireNotEmpty(tenantId);
        return StreamSupport.stream(bookings.spliterator(), false)
                .map(it -> {
                    ValueTypeUtils.requireEmpty(it.getId());
                    var id = BookingId.generate();
                    var doc = new Document();
                    doc.put("_id", id);
                    doc.put("from", it.getFrom());
                    doc.put("to", it.getTo());
                    doc.put("status", it.getStatus());
                    doc.put("note", it.getNote());
                    doc.put("createdAt", it.getCreatedAt());
                    mongoClient.getDatabase("test").getCollection("booking").insertOne(doc);
                    return id;
                }).toList();
    }

    @Override
    public Booking fetchById(TenantId tenantId, BookingId bookingId) {
        //ValueTypeUtils.requireNotEmpty(tenantId);
        ValueTypeUtils.requireNotEmpty(bookingId);
        var collection = mongoClient.getDatabase("test")
                               .getCollection("booking");
        return collection.find(
                Filters.eq("_id", bookingId.getValue()), Booking.class
        ).first();
    }

    @Override
    public Iterable<Booking> fetchAll(TenantId tenantId) {
        //ValueTypeUtils.requireNotEmpty(tenantId)
        var collection = mongoClient.getDatabase("test")
                               .getCollection("booking", Booking.class);
        var bookings = new ArrayList<Booking>();
        return collection.find().into(bookings);
    }

    @Override
    public void deleteAll(TenantId tenantId, Iterable<BookingId> bookingIds) {
        //ValueTypeUtils.requireNotEmpty(tenantId)
        StreamSupport
                .stream(bookingIds.spliterator(), false)
                .forEach(ValueTypeUtils::requireNotEmpty);
        var collection = mongoClient.getDatabase("test").getCollection("booking", Booking.class);
        StreamSupport
                .stream(bookingIds.spliterator(), false)
                .forEach(it -> collection.findOneAndDelete(
                        Filters.eq("_id", it.getValue())
                ));

    }
}
