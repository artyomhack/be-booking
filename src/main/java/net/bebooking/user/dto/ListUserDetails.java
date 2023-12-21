package net.bebooking.user.dto;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public interface ListUserDetails extends Iterable<ListUserDetails.Item>{

    static ListUserDetails of(Item... users) {
        return of(Arrays.asList(users));
    }

    static ListUserDetails of(Iterable<Item> users) {
        return new ListUserDetails() {
            @NotNull
            @Override
            public Iterator<Item> iterator() {
                return users.iterator();
            }
        };
    }

    @AllArgsConstructor
    class Item {
        final String fullName;
        final String email;
        final String phone;
        final String hashPassword;
    }
}
