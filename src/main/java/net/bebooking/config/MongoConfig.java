package net.bebooking.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.bebooking.booking.dao.BookingRepository;
import net.bebooking.booking.dao.MongoBookingRepository;
import net.bebooking.booking.mapper.BookingCodec;
import net.bebooking.booking.model.Booking;
import net.bebooking.client.dao.ClientRepository;
import net.bebooking.client.dao.MongoClientRepository;
import net.bebooking.client.mapper.ClientCodec;
import net.bebooking.client.model.Client;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    private final CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
    private final Codec<Document> documentCodec = codecRegistry.get(Document.class);

    @Bean
    public MongoClient mongoClient() {
        String username = "root";
        String password = "ASDqwe123";
        String host = "mongo.bebooking.ru";
        int port = 27017;
        String connectionString = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        ConnectionString connection = new ConnectionString(connectionString);


        return MongoClients.create(connection);
    }

    @Bean
    public CodecRegistry bookingCodecRegistry() {
        Codec<Booking> bookingCodec = new BookingCodec(codecRegistry);
        return CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(
                        documentCodec,
                        bookingCodec
                )
        );
    }

    @Bean
    public CodecRegistry clientCodecRegistry() {
        Codec<Client> clientCodec = new ClientCodec(codecRegistry);
        return CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(
                        documentCodec,
                        clientCodec
                )
        );
    }

    @Bean
    public BookingRepository bookingRepository() {
        return new MongoBookingRepository(mongoClient(), bookingCodecRegistry());
    }

    @Bean
    public ClientRepository clientRepository() {
        return new MongoClientRepository(mongoClient(), clientCodecRegistry());
    }
}