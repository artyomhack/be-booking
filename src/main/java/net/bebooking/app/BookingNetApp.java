package net.bebooking.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"net.bebooking"})
public class BookingNetApp {
    public static void main(String[] args) {
        SpringApplication.run(BookingNetApp.class,args);
    }
}