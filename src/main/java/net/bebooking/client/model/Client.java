package net.bebooking.client.model;

import lombok.Getter;
import lombok.Setter;
import org.ecom24.common.utils.ErrorUtils;
import org.ecom24.common.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class Client {

    private final ClientId id;

    private String fullName;

    private String email;

    private String phoneNumber;

    @Setter
    private String description;

    @Setter
    private String country;

    @Setter
    private String city;

    /**
     *  В отдельную сущность
     *  Passport
     *  {
     *      String serialPassport;
     *      String numberPassport;
     *      String placeOfIssue;
     *      String dateOfIssue;
     *      String address;
     *  }
     */

    private final LocalDateTime createdAt;

    public static Client newOf(String fullName, String email, String phoneNumber, String description, String country, String city) {
        return new Client(ClientId.EMPTY, fullName, email, phoneNumber, description, country, city, null);
    }

    public static Client of(ClientId id, String fullName, String email, String phoneNumber, String description, String country, String city, LocalDateTime createdAt) {
        Objects.requireNonNull(id);
        return new Client(id, fullName, email, phoneNumber, description, country, city, createdAt);
    }

    public Client(ClientId id, String fullName, String email, String phoneNumber, String description, String country, String city, LocalDateTime createdAt) {
        this.id = id;
        setFullName(fullName);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        this.description = description;
        this.country = country;
        this.city = city;
        this.createdAt = Objects.requireNonNullElse(createdAt, LocalDateTime.now());
    }

    public void setFullName(String fullName) {
        Objects.requireNonNull(fullName);
        StringUtils.requireNonBlank(fullName);

        String[] partNames = fullName.split("\\s+");
        Arrays.asList(partNames).forEach( it ->
                StringUtils.requireLength(it, 1, 45, "Names exceed maximal length")
        );
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        if (!StringUtils.isEmail(email))
            ErrorUtils.argumentError("Email is not correct");
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!StringUtils.isPhoneNumber(phoneNumber))
            ErrorUtils.argumentError("Phone number is not correct");
        this.phoneNumber = phoneNumber;
    }
}
