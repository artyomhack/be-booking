package net.bebooking.config;


import lombok.Getter;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "bebooking.mongodb")
@Getter
public class MongoProperties {

    @Value("${bebooking.mongodb.host}")
    private  String host;

    @Value("${bebooking.mongodb.port}")
    private  Integer port;

    @Value("${bebooking.mongodb.username}")
    private  String username;

    @Value("${bebooking.mongodb.password}")
    private  String password;

    public String getUrl() {
        return "mongodb://" + username + ":" + password + "@" + host + ":" + port;
    }
}
