package net.bebooking.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.bebooking.booking.dao.BookingRepository;
import net.bebooking.booking.dao.MongoBookingRepository;
import net.bebooking.booking.mapper.BookingCodec;
import net.bebooking.booking.model.Booking;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Bean
    public MongoClient mongoClient() {
        String username = "root";
        String password = "ASDqwe123";
        String host = "89.188.107.13";
        int port = 27017;
        String connectionString = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        ConnectionString connection = new ConnectionString(connectionString);


        return MongoClients.create(connection);
    }

    @Bean
    public CodecRegistry codecRegistry() {
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
        return codecRegistry;
    }

    @Bean
    public BookingRepository bookingRepository() {
        return new MongoBookingRepository(mongoClient(), codecRegistry());
    }
}
