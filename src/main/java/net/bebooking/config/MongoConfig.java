package net.bebooking.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.bebooking.booking.dao.BookingRepository;
import net.bebooking.booking.dao.MongoBookingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Arrays;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        String username = "root";
        String password = "ASDqwe123";
        String host = "89.188.107.13";
        int port = 27017;

        MongoCredential credential = MongoCredential.createCredential(username, getDatabaseName(), password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress(host, port))))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public BookingRepository bookingRepository() {
        return new MongoBookingRepository(mongoClient());
    }
}
