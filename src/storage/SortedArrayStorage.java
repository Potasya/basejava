package storage;

import model.Resume;

import java.util.Arrays;

/**
 * Created by Marisha on 19/02/2018.
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertElement(Resume r, int index) {
        int insIndex = -index-1;
        for (int i = insIndex; i < size; i++) {
            storage[i+1] = storage [i];
        }
        storage[insIndex] = r;
    }

    @Override
    protected void deleteElement(int index) {
        for (int i = index; i < size-1; i++) {
            storage[i] = storage[i+1];
        }
    }


    @Override
    protected Integer getSearchKey(String uuid) {
        Resume r = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, r);
    }
}
