package net.bebooking.book.unit.dao;

import com.mongodb.client.MongoClient;
import net.bebooking.BaseMongoTest;
import net.bebooking.booking.dao.BookingRepository;
import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.config.MongoConfig;
import net.bebooking.config.MongoProperties;
import net.bebooking.tenant.model.TenantId;
import net.bebooking.utils.MongoUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@SpringBootTest
@ContextConfiguration(classes = MongoConfig.class)
public class BookingDAOTest extends BaseMongoTest {
    private final static TenantId tenantTest = MongoUtils.tenantTestOf();
    private final BookingRepository bookingRepository;
    private final MongoClient mongoClient;

    //Не видит без аннотации
    @Autowired
    public BookingDAOTest(BookingRepository bookingRepository, MongoClient mongoClient, MongoProperties props) {
        this.bookingRepository = bookingRepository;
        this.mongoClient = mongoClient;
        System.out.println(props.getHost());
        System.out.println(props.getPort());
    }

    @AfterEach
    public void removeCacheEachTest() {
        MongoUtils.cleanCache(mongoClient.getDatabase(tenantTest.getValue().toString())
                .getCollection("booking"));
    }

    @Test
      public void insertAll_success() throws InterruptedException {
        var booking1 = Booking.newOf(Instant.parse("2023-12-25T18:34:56.00Z"), Instant.parse("2024-01-12T08:45:51.00Z"),  "Вид на море");
        var booking2 = Booking.newOf(Instant.parse("2023-07-29T09:22:51.00Z"), Instant.parse("2023-08-06T19:54:06.00Z"), "Два туалета");

        var ids = bookingRepository.insertAll(tenantTest, List.of(booking1, booking2));

        var sizeIds = StreamSupport.stream(ids.spliterator(), false)
                .toList().size();

        Assertions.assertEquals(2, sizeIds);
        Assertions.assertNotNull(BookingId.parseNotEmpty(ids.iterator().next()));
        Assertions.assertNotNull(BookingId.parseNotEmpty(ids.iterator().next()));

        //booking1 = Booking.of(ids.iterator().next, booking1.get..)
        ////booking2 = Booking.of(ids.iterator().next, booking1.get..)
    }

    @Test
    public void fetchById_success() {
        var tempBookings = createTempsBookings(Instant.parse("2023-12-25T18:34:56.00Z"), Instant.parse("2024-01-12T08:45:51.00Z"));
        var id = bookingRepository.insertAll(tenantTest, tempBookings).iterator().next();

        var tempBooking = tempBookings.get(0);
        var testBooking = Booking.of(id, tempBooking.getFrom(), tempBooking.getTo(), tempBooking.getStatus(), tempBooking.getNote(), tempBooking.getCreatedAt());

        var booking = bookingRepository.fetchById(tenantTest, id);

        Assertions.assertNotNull(booking);
        Assertions.assertNotNull(BookingId.parseNotEmpty(booking.getId()));
        Assertions.assertEquals(testBooking, booking);
    }

    @Test
    public void fetchAllByIds_success() {
        var tempsBookings = createTempsBookings(Instant.parse("2023-12-25T18:34:56.00Z"), Instant.parse("2024-01-12T08:45:51.00Z"));
        var ids = StreamSupport.stream(bookingRepository.insertAll(tenantTest, tempsBookings).spliterator(), false).toList();
        var bookingsFetches = ids.stream().map(it -> bookingRepository.fetchById(tenantTest, it)).toList();

        //Добавим лишний объект, он не должен попасть в диапазон fetchAllByIds
        var tempAddBooking = Booking.newOf(Instant.parse("2024-01-07T13:32:08.00Z"), Instant.parse("2024-01-17T22:31:33.00Z"), "До моря 15 минут");
        var id = bookingRepository.insertAll(tenantTest, List.of(tempAddBooking));
        tempAddBooking = Booking.of(id.iterator().next(), tempAddBooking.getFrom(), tempAddBooking.getTo(), tempAddBooking.getStatus(), tempAddBooking.getNote(), tempAddBooking.getCreatedAt());

        var bookings = StreamSupport.stream(bookingRepository.fetchAllByIds(tenantTest, ids).spliterator(), false).toList();

        Assertions.assertEquals(4, ids.size());

        Assertions.assertTrue(bookings.contains(bookingsFetches.get(0)));
        Assertions.assertTrue(bookings.contains(bookingsFetches.get(1)));
        Assertions.assertTrue(bookings.contains(bookingsFetches.get(2)));
        Assertions.assertTrue(bookings.contains(bookingsFetches.get(3)));

        Assertions.assertFalse(bookings.contains(tempAddBooking));
    }

    @Test
    public void fetchAll_success() {
        //На всякий случай очищаем перед заполнением.
        MongoUtils.cleanCache(mongoClient.getDatabase(tenantTest.getValue().toString())
                .getCollection("booking"));

        var tempBookings = StreamSupport.stream(bookingRepository.insertAll(tenantTest, createTempsBookings(Instant.parse("2023-12-25T18:34:56.00Z"),
                Instant.parse("2024-01-12T08:45:51.00Z"))).spliterator(), false)
                .toList().stream()
                .map(it -> bookingRepository.fetchById(tenantTest, it))
                .toList();

        var bookings = StreamSupport.stream(bookingRepository.fetchAll(tenantTest).spliterator(), false).toList();

        Assertions.assertEquals(tempBookings.size(), bookings.size());
        Assertions.assertArrayEquals(tempBookings.toArray(), bookings.toArray());
    }



    @Test
    public void deleteAllByIds_fail() {
        var tempBookings = StreamSupport.stream(bookingRepository.insertAll(tenantTest, createTempsBookings(
                Instant.parse("2023-12-25T18:34:56.00Z"),
                Instant.parse("2024-01-12T08:45:51.00Z")
        )).spliterator(), false).toList();

        Assertions.assertEquals(4, tempBookings.size());
        bookingRepository.deleteAllByIds(tenantTest, tempBookings);

        tempBookings.forEach(booking -> {
            Assertions.assertNull(bookingRepository.fetchById(tenantTest, booking));
        });
    }

    private static List<Booking> createTempsBookings(Instant fromDate, Instant toDate) {
        List<String> descriptions = List.of(
                "Большой зал с ТВ", "Хочу большой зал с ТВ",
                "Можно пиво в холодосе", "Буду с собакой"
        );

        return Stream.iterate(0, i -> i + 1)
                .limit(4)
                .map(i -> Booking.newOf(
                        fromDate.minus(i * 2L, ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant(),
                        toDate.plus(i * 3L, ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant(),
                        descriptions.get(i)
                ))
                .toList();
    }
}
