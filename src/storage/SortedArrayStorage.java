package storage;

import model.Resume;

import java.util.Arrays;

/**
 * Created by Marisha on 19/02/2018.
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            System.out.println("Can't save. There is not enough place in storage.");
            return;
        }
        int index = getIndex(r.getUuid());
        if (index > 0){
            System.out.println("Resume already exists!!!");
            return;
        }
        for (int i = -index-1; i < size; i++) {
            storage[i+1] = storage [i];
        }
        storage[-index-1] = r;
        size++;
    }

    protected int getIndex(String uuid){
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
