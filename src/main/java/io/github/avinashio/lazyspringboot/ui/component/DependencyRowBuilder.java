package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DependencyRowBuilder {

    public List<DependencyRow> build(
            List<DependencyItem> items) {
        List<DependencyRow> rows =
                new ArrayList<>();

        String currentGroup = null;

        for (int index = 0; index < items.size(); index++) {
            DependencyItem item = items.get(index);

            String group =
                    item.dependency().group();

            if (!group.equals(currentGroup)) {
                rows.add(
                        new DependencyRow.GroupHeader(group));

                currentGroup = group;
            }

            rows.add(
                    new DependencyRow.Dependency(
                            index,
                            item));
        }

        return List.copyOf(rows);
    }

    public int findDependencyRowIndex(
            List<DependencyRow> rows,
            int dependencyIndex) {
        for (int rowIndex = 0;
             rowIndex < rows.size();
             rowIndex++) {
            DependencyRow row = rows.get(rowIndex);

            if (row instanceof DependencyRow.Dependency dependency
                    && dependency.dependencyIndex()
                    == dependencyIndex) {
                return rowIndex;
            }
        }

        return 0;
    }
}