package net.bebooking.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;




@ConfigurationProperties(prefix = "bebooking.mongodb")
@AllArgsConstructor
@Getter
public class MongoProperties {

    private final String host;
    private final int port;
    private final String username;
    private final String password;

    public String getUrl() {
        return "mongodb://" + username + ":" + password + "@" + host + ":" + port;
    }
}
