package net.bebooking.company.model;

import lombok.Getter;
import lombok.Setter;
import org.ecom24.common.utils.ErrorUtils;
import org.ecom24.common.utils.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Company {

    private final CompanyId id;

    private final Integer ownerId;

    @Setter
    private String companyName;

    private String email;

    private String phoneNumber;

    @Setter
    private String country;

    @Setter
    private String city;

    @Setter
    private String address;

    private final LocalDateTime createdAt;

    public static Company newOf(String companyName, String email, String phoneNumber, String country, String city, String address) {
        return new Company(CompanyId.EMPTY, null, companyName, email, phoneNumber, country, city, address, null);
    }

    public static Company of(CompanyId id, Integer ownerId, String companyName, String email, String phoneNumber, String country, String city, String address, LocalDateTime createdAt) {
        Objects.requireNonNull(id);
        return new Company(id, ownerId, companyName, email, phoneNumber, country, city, address, createdAt);
    }

    public Company(CompanyId id, Integer ownerId, String companyName, String email, String phoneNumber,
                   String country, String city, String address, LocalDateTime createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.companyName = companyName;
        setEmail(email);
        setPhoneNumber(phoneNumber);
        this.country = country;
        this.city = city;
        this.address = address;
        this.createdAt = Objects.requireNonNullElse(createdAt, LocalDateTime.now());
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
