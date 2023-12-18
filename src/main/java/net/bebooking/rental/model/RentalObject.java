package net.bebooking.rental.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class RentalObject {
    private final RentalObjectId id;

    @Setter
    private String name;

    @Setter
    private String rentalType;

    @Setter
    private String description;

    /** @Setter
    private String additionalEquipment;

        enum AdditionalEquipment {
            доп. оборудование в аренде
        }
     */

    @Setter
    private String routeComment;

    @Setter
    private String address;

    @Setter
    private String postCode;

    @Setter
    private Double latitude;

    @Setter
    private Double longitude;

    private final LocalDateTime createdAt;

    public static RentalObject newOf(String roomName, String roomDescription, String additionalEquipment, String routeComment,
                                     String postCode, Double latitude, Double longitude) {
        return new RentalObject
                (
                RentalObjectId.EMPTY, roomName, roomDescription, additionalEquipment, routeComment,
                         postCode, latitude, longitude, null
                );
    }

    public static RentalObject of (RentalObjectId id, String roomName, String roomDescription, String additionalEquipment, String routeComment,
                                   String postCode, Double latitude, Double longitude, LocalDateTime createdAt) {
        Objects.requireNonNull(id);
        return new RentalObject
                (
                id, roomName, roomDescription, additionalEquipment, routeComment,
                postCode, latitude, longitude, createdAt
                );
    }

    public RentalObject(RentalObjectId id, String name, String description, String routeComment,
                        String address, String postCode, Double latitude, Double longitude, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.routeComment = routeComment;
        this.address = address;
        this.postCode = postCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = Objects.requireNonNullElse(createdAt, LocalDateTime.now());
    }
}
