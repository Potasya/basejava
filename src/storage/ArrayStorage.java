package storage;

import model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            System.out.println("Can't save. There is not enough place in storage.");
            return;
        }
        if (getIndex(r.getUuid()) > 0){
            System.out.println("Resume already exists!!!");
            return;
        }
        storage[size] = r;
        size++;
    }

    protected int getIndex(String uuid){
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())){
                return i;
            }
        }
        return -1;
    }
}
