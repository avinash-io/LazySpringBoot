package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class DependencyFilter {

    public List<DependencyItem> filter(
            List<DependencyItem> items,
            String query) {
        if (query == null || query.isBlank()) {
            return items;
        }

        String normalizedQuery =
                query.toLowerCase(Locale.ROOT);

        return items.stream()
                .filter(
                        item ->
                                matches(
                                        item,
                                        normalizedQuery))
                .toList();
    }

    public List<Integer> matchingIndexes(
            List<DependencyItem> items,
            String query) {
        List<Integer> indexes =
                new ArrayList<>();

        for (int index = 0;
             index < items.size();
             index++) {
            if (matches(
                    items.get(index),
                    normalize(query))) {
                indexes.add(index);
            }
        }

        return List.copyOf(indexes);
    }

    private boolean matches(
            DependencyItem item,
            String query) {
        return normalize(
                item.dependency().name())
                .contains(query)
                || normalize(
                item.dependency().id())
                .contains(query)
                || normalize(
                item.dependency()
                        .description())
                .contains(query)
                || normalize(
                item.dependency().group())
                .contains(query);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return value.toLowerCase(Locale.ROOT);
    }
}