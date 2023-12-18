package net.bebooking.client.dto;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public interface ClientList extends Iterable<ClientList.Item> {

    static ClientList of(Item ...client) {
        return of(Arrays.asList(client));
    }

    static ClientList of(Iterable<Item> clients) {
        return new ClientList() {
            @NotNull
            @Override
            public Iterator<Item> iterator() {
                return clients.iterator();
            }
        };
    }

    @AllArgsConstructor
    class Item {
        final String fullName;

        final String email;

        final String phone;
    }
}
