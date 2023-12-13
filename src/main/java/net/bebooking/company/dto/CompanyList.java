package net.bebooking.company.dto;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public interface CompanyList extends Iterable<CompanyList.Item> {

    static CompanyList of (Item ...companies) {
        return of(Arrays.asList(companies));
    }

    static CompanyList of(Iterable<Item> companies) {
        return new CompanyList() {
            @NotNull
            @Override
            public Iterator<Item> iterator() {
                return companies.iterator();
            }
        };
    }

    @AllArgsConstructor
    class Item {
        final String companyName;
        final String email;
        final String phone;
    }
}
