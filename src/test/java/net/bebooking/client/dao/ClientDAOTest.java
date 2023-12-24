package net.bebooking.client.dao;

import com.mongodb.client.MongoClient;
import net.bebooking.MongoTestContainerConfig;
import net.bebooking.client.model.Client;
import net.bebooking.client.model.ClientId;
import net.bebooking.config.MongoConfig;
import net.bebooking.tenant.model.TenantId;
import net.bebooking.utils.MongoUtils;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SpringBootTest
@ContextConfiguration(classes = MongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientDAOTest {

    private final ClientRepository clientRepository;

    private final MongoClient mongoClient;

    private final static TenantId tenantTest = MongoUtils.tenantTestOf();

    @Autowired
    public ClientDAOTest(ClientRepository clientRepository, MongoClient mongoClient) {
        this.clientRepository = clientRepository;
        this.mongoClient = mongoClient;
    }

    @AfterEach
    public void removeCache() {
        MongoUtils.cleanCache(mongoClient.getDatabase(tenantTest.getValue().toString())
                .getCollection("client"));
    }

    @AfterAll
    public void removeAll() {
        MongoUtils.dropData(mongoClient, tenantTest.getValue().toString());
    }

    @Test
    public void insertAll_success() {
        var client1 = Client.newOf("Артём Хакимуллин", "xakim@gmail.com", "79961232970",
                "Любит колу", "Russia", "Kazan");
        var client2 = Client.newOf("Камилла Шакирова", "kami1234@gmail.com", "79953362750",
                "Любит смотреть сериалы", "Russia", "Kazan");

        var ids = StreamSupport.stream(clientRepository.insertAll(tenantTest, List.of(client1, client2))
                        .spliterator(), false)
                        .toList();

        Assert.assertEquals(2, ids.size());
        Assert.assertNotNull(ClientId.parseNotEmpty(ids.get(0)));
        Assert.assertNotNull(ClientId.parseNotEmpty(ids.get(1)));
    }

    @Test
    public void fetchById_success() {
        var ids = StreamSupport.stream(clientRepository.insertAll(tenantTest, createTempClients()).spliterator(), false)
                .toList();
        var clientId = ids.get(2);

        var testClient = clientRepository.fetchById(tenantTest, clientId);
        Assert.assertNotNull(testClient);

        var client = Client.of(clientId, testClient.getFullName(), testClient.getEmail(), testClient.getPhoneNumber(),
                testClient.getDescription(), testClient.getCountry(), testClient.getCity(), testClient.getCreatedAt());

        Assert.assertEquals(client, testClient);
    }

    @Test
    public void fetchAll_success() {
        MongoUtils.cleanCache(mongoClient.getDatabase(tenantTest.getValue().toString())
                .getCollection("client"));

        var ids = StreamSupport.stream(clientRepository.insertAll(tenantTest, createTempClients()).spliterator(), false)
                .toList();

        var testClients = StreamSupport.stream(clientRepository.fetchAll(tenantTest).spliterator(), false)
                .toArray();

        Assert.assertEquals(ids.size(), testClients.length);

        var clients = Stream.iterate(0, i -> i + 1)
                .limit(ids.size())
                .map(i -> clientRepository.fetchById(tenantTest, ids.get(i)))
                .toArray();

        Assert.assertArrayEquals(testClients, clients);
    }

    @Test
    public void fetchAllByIds_success() {
        var ids = StreamSupport.stream(clientRepository.insertAll(tenantTest, createTempClients()).spliterator(), false)
                .toList();
        var clients = ids.stream().map(it -> clientRepository.fetchById(tenantTest, it)).toList();

        var excessId = clientRepository.insertAll(tenantTest, List.of(Client.newOf("Миша Кулаков", "mihail@gmail.com",
                "79965465678", "Опаздывает с оплатой", "Russia", "Kazan")));
        var excessClient = clientRepository.fetchById(tenantTest, excessId.iterator().next());

        var testClients = StreamSupport.stream(clientRepository.fetchAllByIds(tenantTest, ids).spliterator(), false)
                .toList();

        Assert.assertEquals(ids.size(), testClients.size());
        Assert.assertFalse(testClients.contains(excessClient));

        Assert.assertTrue(testClients.contains(clients.get(0)));
        Assert.assertTrue(testClients.contains(clients.get(1)));
        Assert.assertTrue(testClients.contains(clients.get(2)));
        Assert.assertTrue(testClients.contains(clients.get(3)));
    }

    @Test
    public void deleteAllByIds_success() {
        var ids = StreamSupport.stream(clientRepository.insertAll(tenantTest, createTempClients()).spliterator(), false)
                .toList();

        var excessId = clientRepository.insertAll(tenantTest, List.of(Client.newOf("Миша Кулаков", "mihail@gmail.com",
                "79965465678", "Опаздывает с оплатой", "Russia", "Kazan")));
        var excessClient = clientRepository.fetchById(tenantTest, excessId.iterator().next());

        clientRepository.deleteAllByIds(tenantTest, ids);

        var clients = StreamSupport.stream(clientRepository.fetchAllByIds(tenantTest, ids).spliterator(), false)
                .toList();

        Assert.assertTrue(clients.isEmpty());

        clients = StreamSupport.stream(clientRepository.fetchAll(tenantTest).spliterator(), false)
                .toList();

        Assert.assertEquals(1, clients.size());
        Assert.assertTrue(clients.contains(excessClient));
    }

    private List<Client> createTempClients() {
        return List.of(
                Client.newOf("Вася Пупкин", "ivan90@gmail.com", "79182033456",
                        "Частый клиент" ,"Russia", "Moscow"),
                Client.newOf("Петя Душиков", "petyy@mail.com", "79345677654",
                        "Хочет изменить расстановку в комнате", "Russia", "Omsk"),
                Client.newOf("Катя Чайка", "katyCh@gmail.com", "79863452333",
                        "Отдать сдачу", "Russia", "Sochi"),
                Client.newOf("Ивану Иванов", "ivanka@gmail.com", "79963212970",
                        "Убрать комнату", "Russia", "Volgograd")
        );
    }
}
