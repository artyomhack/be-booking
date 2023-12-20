package net.bebooking.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.bebooking.booking.dao.BookingRepository;
import net.bebooking.booking.dao.MongoBookingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
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
    public BookingRepository bookingRepository() {
        return new MongoBookingRepository(mongoClient());
    }
}