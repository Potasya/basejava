package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Marisha on 25/02/2018.
 */
public class ListSection<T>  extends Section{
    private final List<T> items;

    public ListSection(T... items) {
        Objects.requireNonNull(items, "List items mustn't be null");
        this.items = Arrays.asList(items);
    }

    public void addItemToList(T item){
        items.add(item);
    }

    public List<T> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection<?> that = (ListSection<?>) o;

        return items.equals(that.items);

    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
