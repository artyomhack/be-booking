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
import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.ecom24.common.types.ValueTypeUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.print.Book;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MongoConfig.class)
public class BookDAOTest {

    @Autowired
    private MongoClient mongoClient;

    private MongoDatabase mongoDatabase;

    @BeforeEach
    public void setUp() {
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
        mongoDatabase = mongoClient.getDatabase("booking_test")
                .withCodecRegistry(codecRegistry);
    }

    @Test
    public void insertAllShouldSaveBooking_checkWithHelpFetchById() {
        var euDate = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String fromDate = "09.12.2023 12:34:56";
        String toDate = "11.01.2024 18:45:30";
        var from = LocalDateTime.parse(fromDate, euDate);
        var to = LocalDateTime.parse(toDate, euDate);

        var createdAt = LocalDateTime.now();
        Booking booking_1 = Booking.newOf(from, to, "Вид из окна на море");

        var id = StreamSupport.stream(
                insertAll(List.of(booking_1)).spliterator(), false)
                .findFirst()
                .orElseThrow();

        var returnedBooking = fetchById(id);

        Assert.assertNotNull(returnedBooking);
        Assert.assertEquals(id.getValue(), returnedBooking.getId().getValue());
        Assert.assertEquals(booking_1.getFrom(), returnedBooking.getFrom());
        Assert.assertEquals(booking_1.getTo(), returnedBooking.getTo());
        Assert.assertEquals(booking_1.getStatus(), returnedBooking.getStatus());
        Assert.assertEquals("Вид из окна на море", returnedBooking.getNote());
        Assert.assertNotNull(returnedBooking.getCreatedAt());
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
                    var id = BookingId.generate();
                    var doc = new Document();
                    doc.put("_id", id.getValue());
                    doc.put("from", it.getFrom());
                    doc.put("to", it.getTo());
                    doc.put("status", it.getStatus().toString());
                    doc.put("note", it.getNote());
                    doc.put("createdAt", it.getCreatedAt());
                    mongoDatabase.getCollection("booking").insertOne(doc);
                    return id;
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


}
