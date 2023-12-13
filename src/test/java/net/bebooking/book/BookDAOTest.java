package net.bebooking.book;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.booking.model.BookingStatus;
import net.bebooking.config.MongoConfig;
import org.bson.Document;
import org.ecom24.common.types.ValueTypeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MongoConfig.class)
@Transactional
public class BookDAOTest {

    @Autowired
    private MongoClient mongoClient;

//    private BookingRepository bookingRepository;
//
//    @BeforeEach
//    public void setUp() {
//        bookingRepository = new MongoBookingRepository(mongoClient);
//    }

    @Test
    @Rollback
    public void insertAllShouldSaveBooking_checkWithHelpFetchById() {
        var id = BookingId.generate();
        var dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var to = LocalDateTime.parse("2023-12-09", dateFormat);
        var from = LocalDateTime.parse("2024-01-11", dateFormat);
        var createdAt = LocalDateTime.now();
        Booking booking_1 = Booking.of(id, to, from, BookingStatus.CREATED,
                                    "Вид из окна на море", createdAt);

        //Проверяю на правильность передачи переменных в объект
        Assert.assertNotNull(booking_1.getId());
        Assert.assertEquals(id, booking_1.getId());
        Assert.assertEquals(to, booking_1.getTo());
        Assert.assertEquals(from, booking_1.getFrom());
        Assert.assertEquals(BookingStatus.CREATED, booking_1.getStatus());
        Assert.assertEquals("Вид из окна на море", booking_1.getNote());

        //Вставляю данные в mongo
        insertAll(List.of(booking_1));

        var returnedBooking = fetchById(id);

        Assert.assertNotNull(returnedBooking);
        Assert.assertEquals(booking_1.getId(), returnedBooking.getId());
        Assert.assertEquals(booking_1.getTo(), returnedBooking.getTo());
        Assert.assertEquals(booking_1.getFrom(), returnedBooking.getFrom());
        Assert.assertEquals(booking_1.getStatus(), returnedBooking.getStatus());
        Assert.assertEquals("Вид из окна на море", returnedBooking.getNote());
        Assert.assertEquals(createdAt, returnedBooking.getCreatedAt());
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

    private Iterable<BookingId> insertAll(Iterable<Booking> bookings) {
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
                    mongoClient.getDatabase("tenant").getCollection("booking").insertOne(doc);
                    return id;
                }).toList();
    }

    private Booking fetchById(BookingId bookingId) {
        ValueTypeUtils.requireNotEmpty(bookingId);
        var collection = mongoClient.getDatabase("be_booking")
                .getCollection("booking");
        return collection.find(
                Filters.eq("_id", bookingId.getValue()), Booking.class
        ).first();
    }

}
