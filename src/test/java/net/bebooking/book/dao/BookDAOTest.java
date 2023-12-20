package net.bebooking.book.dao;

import com.mongodb.client.MongoClient;
import net.bebooking.booking.dao.BookingRepository;
import net.bebooking.booking.model.Booking;
import net.bebooking.config.MongoConfig;
import net.bebooking.tenant.model.TenantId;
import net.bebooking.utils.MongoUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    @Autowired
    public BookDAOTest(BookingRepository repository, MongoClient mongoClient) {
        this.repository = repository;
        this.mongoClient = mongoClient;
    }

    @BeforeAll
    public void setUp() {

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

        var exists = StreamSupport.stream(repository.fetchAllByIds(testTenant, ids).spliterator(), false)
                .toList();

        Assert.assertEquals(2, exists.size());
        Assert.assertNotNull(exists.get(0).getId());
        Assert.assertNotNull(exists.get(1).getId());
        Assert.assertEquals(exists.get(0).getFrom(), booking1.getFrom());
        Assert.assertEquals(exists.get(0).getTo(), booking1.getTo());
        Assert.assertEquals(exists.get(0).getNote(), booking1.getNote());
        Assert.assertEquals(exists.get(1).getFrom(), booking2.getFrom());
        Assert.assertEquals(exists.get(1).getTo(), booking2.getTo());
        Assert.assertEquals(exists.get(1).getNote(), booking2.getNote());

        //equals
        MongoUtils.cleanCache(mongoClient.getDatabase(testTenant.getValue().toString())
                  .getCollection("booking"));
    }

    private static List<Booking> createTempsBookings(Instant fromDate, Instant toDate) {
//        var fromDate = Instant.parse("2023-12-25T18:34:56").toString();
//        Strvaring toDate = "2024-01-11 11:45:30";
//        LocalDateTime from = LocalDateTime.parse(fromDate, format);
//        LocalDateTime to = LocalDateTime.parse(toDate, format);

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
