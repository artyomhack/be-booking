package net.bebooking.client.mapper;

import net.bebooking.client.model.Client;
import net.bebooking.client.model.ClientId;
import org.bson.Document;

import javax.print.Doc;
import java.time.ZoneId;
import java.util.Objects;

public class ClientConverter {

    public Document convert(Client client) {
        var id = Objects.requireNonNullElse(client.getId().getValueOrNull(), ClientId.generate().getValue());
        return new Document()
                .append("_id", id)
                .append("fullName", client.getFullName())
                .append("email", client.getEmail())
                .append("phoneNumber", client.getPhoneNumber())
                .append("description", client.getDescription())
                .append("country", client.getCountry())
                .append("city", client.getCity())
                .append("createdAt", client.getCreatedAt());
    }

    public Client convert(Document document) {
        return Client.of(
                ClientId.parseNotEmpty(document.get("_id")),
                document.getString("fullName"),
                document.getString("email"),
                document.getString("phoneNumber"),
                document.getString("description"),
                document.getString("country"),
                document.getString("city"),
                document.getDate("createdAt")
                                 .toInstant()
                                 .atZone(ZoneId.systemDefault())
                                 .toLocalDateTime()

        );
    }
}
