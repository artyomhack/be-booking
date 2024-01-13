package net.bebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"net.bebooking"})
@ConfigurationPropertiesScan
public class BookingNetApp {
    public static void main(String[] args) {
        SpringApplication.run(BookingNetApp.class,args);
    }
}