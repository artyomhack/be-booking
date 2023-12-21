package net.bebooking.user.model;

import lombok.Getter;
import lombok.Setter;
import org.ecom24.common.utils.ErrorUtils;
import org.ecom24.common.utils.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class User {
    private final UserId id;

    private String fullName;

    private String email;

    private String phone;

    @Setter
    private String hashPassword;

    @Setter
    private ZoneId zoneId;

    private final  LocalDateTime createdAt;

    public static User newOf(String fullName, String email, String hashPassword, ZoneId zoneId) {
        return new User(UserId.EMPTY, fullName, email, hashPassword, zoneId, null);
    }

    public static User of(UserId id, String fullName, String email, String hashPassword, ZoneId zoneId, LocalDateTime createdAt) {
        Objects.requireNonNull(id);
        return new User(id, fullName, email, hashPassword, zoneId, createdAt);
    }

    public User(UserId id, String fullName, String email, String hashPassword, ZoneId zoneId, LocalDateTime createdAt) {
        this.id = id;
        setFullName(fullName);
        setEmail(email);
        this.hashPassword = hashPassword;
        this.zoneId = zoneId;
        this.createdAt = Objects.requireNonNullElse(createdAt, LocalDateTime.now());
    }

    public void setFullName(String fullName) {
        Objects.requireNonNull(fullName);
        StringUtils.requireNonBlank(fullName);

        String[] partNames = fullName.split("\\s+");
        Arrays.asList(partNames).forEach(it ->
                StringUtils.requireLength(it, 1, 45, "Names exceed maximum length")
        );
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        if (!StringUtils.isEmail(email))
            ErrorUtils.argumentError("Email is not correct");
        this.email = email;
    }

    public void setPhone(String phone) {
        if (!StringUtils.isPhoneNumber(phone))
            ErrorUtils.argumentError("Phone number is not correct");
        this.phone = phone;
    }
}
