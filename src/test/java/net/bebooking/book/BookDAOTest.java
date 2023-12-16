package net.bebooking.book;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.bebooking.booking.mapper.BookingCodec;
import net.bebooking.booking.mapper.BookingConverter;
import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.config.MongoConfig;
import net.bebooking.utils.MongoUtils;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.ecom24.common.types.AbstractType;
import org.ecom24.common.types.ValueTypeUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MongoConfig.class)
public class BookDAOTest {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    @Autowired
    public BookDAOTest(MongoClient mongoClient) {
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
        mongoDatabase = this.mongoClient.getDatabase("booking_test")
                .withCodecRegistry(codecRegistry);
    }

    @BeforeEach
    public void clearCache() {
        MongoUtils.cleanCollection(mongoDatabase.getCollection("booking"));
    }

    @Test
    public void insertAllShouldSaveBooking_checkWithHelpFetchById() {
        Booking booking_1 = createTempsBookings().get(0);
        var id = StreamSupport.stream(insertAll(List.of(booking_1)).spliterator(), false)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Object is null"));

        var returnedBooking = fetchById(id);

        Assert.assertNotNull(returnedBooking);
        Assert.assertEquals(id.getValue(), returnedBooking.getId().getValue());
        Assert.assertEquals(booking_1.getFrom(), returnedBooking.getFrom());
        Assert.assertEquals(booking_1.getTo(), returnedBooking.getTo());
        Assert.assertEquals(booking_1.getStatus(), returnedBooking.getStatus());
        Assert.assertEquals(booking_1.getNote(), returnedBooking.getNote());
        Assert.assertNotNull(returnedBooking.getCreatedAt());
    }

    @Test
    public void fetchAllShouldReadBookings_checkWithHelperInsertAll() {
        /*
        Очищаем и добавляем bookings в mongo
         */
        var bookingIds = StreamSupport.stream(insertAll(createTempsBookings()).spliterator(), false)
                                    .map(AbstractType::getValue)
                                    .toList();

        var bookings = StreamSupport.stream(fetchAll().spliterator(), false)
                                    .map(it -> it.getId().getValue())
                                    .toList();

        Assert.assertArrayEquals(bookingIds.toArray(), bookings.toArray());
    }

    @Test
    public void deleteAllShouldClearBookings_checkWithHelperInsertAllAndFetchAll() {
        var bookingIds = insertAll(createTempsBookings());

        deleteAll(bookingIds);

        var bookings = StreamSupport.stream(fetchAll().spliterator(), false)
                        .map(it -> Booking.of(
                                it.getId(),
                                it.getFrom(),
                                it.getTo(),
                                it.getStatus(),
                                it.getNote(),
                                it.getCreatedAt()))
                        .toList();

        Assert.assertEquals(true, bookings.isEmpty());
    }

    /**
     *
     * @param bookings
     * TenantId ещё не разработан, создал методы,
     * которые работают и выполняю задачу ту же самую,
     * но использовал только один параметр.
     * (сущность, которую нужно использовать в бд)
     * @return
     */

    //WORK
    private Iterable<BookingId> insertAll(Iterable<Booking> bookings) {
        return StreamSupport.stream(bookings.spliterator(), false)
                .map(it -> {
                    ValueTypeUtils.requireEmpty(it.getId());
                    var doc = new BookingConverter().convert(it);
                    mongoDatabase.getCollection("booking").insertOne(doc);
                    return BookingId.parseNotEmpty(doc.get("_id"));
                })
                .toList();
    }

    //NEED MAPPER
    private Booking fetchById(BookingId bookingId) {
        ValueTypeUtils.requireNotEmpty(bookingId);
        return mongoDatabase
                .getCollection("booking")
                .find(Filters.eq("_id", bookingId.getValue()), Booking.class)
                .first();
    }


    public Iterable<Booking> fetchAll() {
        //ValueTypeUtils.requireNotEmpty(tenantId)
        var collection = mongoDatabase.getCollection("booking", Booking.class);
        var bookings = new ArrayList<Booking>();
        return collection.find().into(bookings);
    }

    public void deleteAll(Iterable<BookingId> bookingIds) {
        //ValueTypeUtils.requireNotEmpty(tenantId)
        StreamSupport.stream(bookingIds.spliterator(), false)
                     .forEach(ValueTypeUtils::requireNotEmpty);

        var collection = mongoDatabase.getCollection("booking", Booking.class);

        StreamSupport.stream(bookingIds.spliterator(), false)
                     .forEach(it -> collection.findOneAndDelete(
                        Filters.eq("_id", it.getValue())
                     ));
    }


    private static List<Booking> createTempsBookings() {
        String fromDate = "2023-12-25 18:34:56";
        String toDate = "2024-01-11 11:45:30";
        var format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var from = LocalDateTime.parse(fromDate, format);
        var to = LocalDateTime.parse(toDate, format);

        return List.of(
                Booking.newOf(from, to, "Большой зал с ТВ"),
                Booking.newOf(from.plusHours(4), to.plusHours(1), "Хочу большой зал с ТВ"),
                Booking.newOf(from.plusHours(12), to.plusHours(4), "Можно пиво в холодосе"),
                Booking.newOf(from.plusHours(16), to.plusHours(24), "Буду с собакой")

        );
    }
}
