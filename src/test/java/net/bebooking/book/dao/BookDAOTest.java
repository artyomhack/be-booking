package net.bebooking.book.dao;

import com.mongodb.client.MongoClient;
import net.bebooking.booking.dao.BookingRepository;
import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import net.bebooking.config.MongoConfig;
import net.bebooking.tenant.model.TenantId;
import net.bebooking.utils.MongoUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookDAOTest {
    private final BookingRepository repository;

    private final MongoClient mongoClient;
    private final static TenantId testTenant = MongoUtils.tenantTestOf();

    //Не видит без аннотации
    @Autowired
    public BookDAOTest(BookingRepository repository, MongoClient mongoClient) {
        this.repository = repository;
        this.mongoClient = mongoClient;
    }

    @AfterEach
    public void removeCacheEachTest() {
        MongoUtils.cleanCache(mongoClient.getDatabase(testTenant.getValue().toString())
                .getCollection("booking"));
    }

    @AfterAll
    public void removeAll() {
        MongoUtils.dropData(mongoClient, testTenant);
    }

    @Test
    public void insertAll_success() {
        var booking1 = Booking.newOf(Instant.parse("2023-12-25T18:34:56.00Z"), Instant.parse("2024-01-12T08:45:51.00Z"),  "Вид на море");
        var booking2 = Booking.newOf(Instant.parse("2023-07-29T09:22:51.00Z"), Instant.parse("2023-08-06T19:54:06.00Z"), "Два туалета");

        var ids = repository.insertAll(testTenant, List.of(booking1, booking2));

        var sizeIds = StreamSupport.stream(ids.spliterator(), false)
                .toList().size();

        Assert.assertEquals(2, sizeIds);
        Assert.assertNotNull(BookingId.parseNotEmpty(ids.iterator().next()));
        Assert.assertNotNull(BookingId.parseNotEmpty(ids.iterator().next()));
    }

    @Test
    public void fetchById_success() {
        var tempBookings = createTempsBookings(Instant.parse("2023-12-25T18:34:56.00Z"), Instant.parse("2024-01-12T08:45:51.00Z"));
        var id = repository.insertAll(testTenant, tempBookings).iterator().next();

        var tempBooking = tempBookings.get(0);
        var testBooking = Booking.of(id, tempBooking.getFrom(), tempBooking.getTo(), tempBooking.getStatus(), tempBooking.getNote(), tempBooking.getCreatedAt());

        var booking = repository.fetchById(testTenant, id);

        Assert.assertNotNull(booking);
        Assert.assertNotNull(BookingId.parseNotEmpty(booking.getId()));
        Assert.assertEquals(testBooking, booking);
    }

    @Test
    public void fetchAllByIds_success() {
        var tempsBookings = createTempsBookings(Instant.parse("2023-12-25T18:34:56.00Z"), Instant.parse("2024-01-12T08:45:51.00Z"));
        var ids = StreamSupport.stream(repository.insertAll(testTenant, tempsBookings).spliterator(), false).toList();
        var bookingsFetches = ids.stream().map(it -> repository.fetchById(testTenant, it)).toList();

        //Добавим лишний объект, он не должен попасть в диапазон fetchAllByIds
        var tempAddBooking = Booking.newOf(Instant.parse("2024-01-07T13:32:08.00Z"), Instant.parse("2024-01-17T22:31:33.00Z"), "До моря 15 минут");
        var id = repository.insertAll(testTenant, List.of(tempAddBooking));
        tempAddBooking = Booking.of(id.iterator().next(), tempAddBooking.getFrom(), tempAddBooking.getTo(), tempAddBooking.getStatus(), tempAddBooking.getNote(), tempAddBooking.getCreatedAt());

        var bookings = StreamSupport.stream(repository.fetchAllByIds(testTenant, ids).spliterator(), false).toList();

        Assert.assertEquals(4, ids.size());

        Assert.assertTrue(bookings.contains(bookingsFetches.get(0)));
        Assert.assertTrue(bookings.contains(bookingsFetches.get(1)));
        Assert.assertTrue(bookings.contains(bookingsFetches.get(2)));
        Assert.assertTrue(bookings.contains(bookingsFetches.get(3)));

        Assert.assertFalse(bookings.contains(tempAddBooking));
    }

    @Test
    public void fetchAll_success() {
        //На всякий случай очищаем перед заполнением.
        MongoUtils.cleanCache(mongoClient.getDatabase(testTenant.getValue().toString())
                .getCollection("booking"));

        var tempBookings = StreamSupport.stream(repository.insertAll(testTenant, createTempsBookings(Instant.parse("2023-12-25T18:34:56.00Z"),
                Instant.parse("2024-01-12T08:45:51.00Z"))).spliterator(), false)
                .toList().stream()
                .map(it -> repository.fetchById(testTenant, it))
                .toList();

        var bookings = StreamSupport.stream(repository.fetchAll(testTenant).spliterator(), false).toList();

        Assert.assertEquals(tempBookings.size(), bookings.size());
        Assert.assertArrayEquals(tempBookings.toArray(), bookings.toArray());
    }



    @Test
    public void deleteAll_fail() {
        var tempBookings = StreamSupport.stream(repository.insertAll(testTenant, createTempsBookings(
                Instant.parse("2023-12-25T18:34:56.00Z"),
                Instant.parse("2024-01-12T08:45:51.00Z")
        )).spliterator(), false).toList();

        Assert.assertEquals(4, tempBookings.size());
        repository.deleteAllByIds(testTenant, tempBookings);

        tempBookings.forEach(booking -> {
            Assert.assertNull(repository.fetchById(testTenant, booking));
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
