import model.Resume;
import storage.ArrayStorage;
import storage.SortedArrayStorage;
import storage.Storage;

import java.util.ArrayList;

/**
 * Test for com.urise.webapp.storage.storage.ArrayStorage
 */
public class MainTestArrayStorage {
    private static final Storage ARRAY_STORAGE = new ArrayStorage();
    private static final Storage SORTED_ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        final Resume r1 = new Resume("uuid1");
        final Resume r2 = new Resume("uuid2");
        final Resume r3 = new Resume("uuid3");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r2);

        SORTED_ARRAY_STORAGE.save(r1);
        SORTED_ARRAY_STORAGE.save(r3);
        SORTED_ARRAY_STORAGE.save(r2);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.getSize());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        System.out.println("Get r1: " + SORTED_ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + SORTED_ARRAY_STORAGE.getSize());

        System.out.println("Get dummy: " + SORTED_ARRAY_STORAGE.get("dummy"));

        printAll(ARRAY_STORAGE);
        printAll(SORTED_ARRAY_STORAGE);
        ARRAY_STORAGE.delete(r1.getUuid());
        SORTED_ARRAY_STORAGE.delete(r1.getUuid());
        printAll(ARRAY_STORAGE);
        printAll(SORTED_ARRAY_STORAGE);
        ARRAY_STORAGE.clear();
        SORTED_ARRAY_STORAGE.clear();
        printAll(ARRAY_STORAGE);
        printAll(SORTED_ARRAY_STORAGE);

        System.out.println("Size: " + ARRAY_STORAGE.getSize());
        System.out.println("Size: " + SORTED_ARRAY_STORAGE.getSize());
    }

    static void printAll(Storage storage) {
        System.out.println("\nGet All");
        for (Resume r : storage.getAll()) {
            System.out.println(r);
        }
    }
}
