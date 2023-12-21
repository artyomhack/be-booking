package net.bebooking.booking.dao;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import net.bebooking.booking.mapper.BookingCodec;
import net.bebooking.booking.mapper.BookingConverter;
import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.tenant.model.TenantId;
import net.bebooking.utils.MongoUtils;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.ecom24.common.types.AbstractType;
import org.ecom24.common.types.ValueTypeUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Repository
public class MongoBookingRepository implements BookingRepository {

    private final MongoClient mongoClient;
    private final CodecRegistry codecRegistry;

    public MongoBookingRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;

        CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Codec<Booking> bookingCodec = new BookingCodec(codecRegistry);

        codecRegistry =  CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(
                        documentCodec,
                        bookingCodec
                )
        );
        this.codecRegistry = codecRegistry;
    }

    @Override
    public Iterable<BookingId> insertAll(TenantId tenantId, Iterable<Booking> bookings) {
        ValueTypeUtils.requireNotEmpty(tenantId);
        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = MongoUtils.requireCollection(data, "booking");
        return StreamSupport.stream(bookings.spliterator(), false)
                .map(it -> {
                    ValueTypeUtils.requireEmpty(it.getId());
                    var doc = new BookingConverter().convert(it);
                    collection.insertOne(doc);
                    return BookingId.parseNotEmpty(doc.get("_id", UUID.class));
                }).toList();
    }

    @Override
    public Booking fetchById(TenantId tenantId, BookingId bookingId) {
        ValueTypeUtils.requireNotEmpty(tenantId);
        ValueTypeUtils.requireNotEmpty(bookingId);
        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = MongoUtils.requireCollection(data,"booking");
        return collection.find(
                Filters.eq("_id", bookingId.getValue()), Booking.class
        ).first();
    }

    @Override
    public Iterable<Booking> fetchAll(TenantId tenantId) {
        ValueTypeUtils.requireNotEmpty(tenantId);
        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = MongoUtils.requireCollection(data, "booking").withDocumentClass(Booking.class);
        var bookings = new ArrayList<Booking>();
        return collection.find().into(bookings)
                .stream()
                .toList();
    }

    @Override
    public Iterable<Booking> fetchAllByIds(TenantId tenantId, Iterable<BookingId> bookingIds) {
        ValueTypeUtils.requireNotEmpty(tenantId);
        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = MongoUtils.requireCollection(data, "booking").withDocumentClass(Booking.class);

        var ids = StreamSupport.stream(bookingIds.spliterator(), false)
                .map(it -> fetchById(tenantId, it).getId().getValue())
                .toList();

        var filter = new Document("_id", new Document("$in", ids));
        var bookings = new ArrayList<Booking>();

        return collection.find(filter).into(bookings)
                .stream()
                .toList();
    }

    @Override
    public void deleteAllByIds(TenantId tenantId, Iterable<BookingId> bookingIds) {
        ValueTypeUtils.requireNotEmpty(tenantId);
        StreamSupport.stream(bookingIds.spliterator(), false)
                     .forEach(ValueTypeUtils::requireNotEmpty);

        var data = mongoClient.getDatabase(tenantId.getValue().toString()).withCodecRegistry(codecRegistry);
        var collection = MongoUtils.requireCollection(data, "booking").withDocumentClass(Booking.class);

        var ids = StreamSupport.stream(bookingIds.spliterator(), false)
                        .map(AbstractType::getValue).toList();
        var filter = new Document("_id", new Document("$in", ids));
        collection.deleteMany(filter);
    }
}
