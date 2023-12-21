package net.bebooking.rental.dto;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public interface RentalObjectList extends Iterable<RentalObjectList.Item>{

    static RentalObjectList of(Item ...rentalObjects) {
        return of(Arrays.asList(rentalObjects));
    }

    static RentalObjectList of(Iterable<Item> rentalObjects) {
        return new RentalObjectList() {
            @NotNull
            @Override
            public Iterator<Item> iterator() {
                return rentalObjects.iterator();
            }
        };
    }

    @AllArgsConstructor
    class Item {
        final String name;
        final String rentalType;
        final String description;
        final String address;
        final String status;
    }
}
