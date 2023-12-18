package net.bebooking.client;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.bebooking.client.mapper.ClientConverter;
import net.bebooking.client.model.Client;
import net.bebooking.client.model.ClientId;
import net.bebooking.config.MongoConfig;
import net.bebooking.tenant.model.TenantId;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.ecom24.common.types.ValueTypeUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoAutoConfiguration.class)
@ContextConfiguration(classes = MongoConfig.class)
public class ClientDAOTest {

    private final MongoDatabase mongoDatabase;

    @Autowired
    public ClientDAOTest(MongoClient mongoClient) {
        CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        Codec<Client> clientCodec = codecRegistry.get(Client.class);

        codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(documentCodec, clientCodec)
        );

        mongoDatabase = mongoClient.getDatabase("booking_test")
                                   .withCodecRegistry(codecRegistry);
    }


    public Iterable<ClientId> insertAll(Iterable<Client> clients) {
        return StreamSupport.stream(clients.spliterator(), false).map(it -> {
            ValueTypeUtils.requireEmpty(it.getId());
            var doc = new ClientConverter().convert(it);
            mongoDatabase.getCollection("client").insertOne(doc);
            return ClientId.parseNotEmpty(doc.get("_id"));
        }).toList();
    }
}
