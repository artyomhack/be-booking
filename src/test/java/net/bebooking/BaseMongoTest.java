package net.bebooking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@ActiveProfiles("integration")
public abstract class BaseMongoTest {
    private final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.4");

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "bebooking.mongo.username",
                () -> mongoDBContainer.getEnvMap().get("MONGO_INITDB_ROOT_USERNAME")
        );

        registry.add(
                "bebooking.mongo.password",
                () -> mongoDBContainer.getEnvMap().get("MONGO_INITDB_ROOT_PASSWORD")
        );

        registry.add(
                "bebooking.mongo.host",
                mongoDBContainer::getHost
        );
        registry.add(
                "bebooking.mongo.port", () -> 2380
        );
    }

    @BeforeAll
    public static void beforeStart() {;
        mongoDBContainer.setPortBindings(List.of("2380:27071"));
        mongoDBContainer.start();
    }

    @Test
    void test() {

    }
}
