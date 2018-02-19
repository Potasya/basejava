package storage;

import model.Resume;

import java.util.Arrays;

/**
 * Created by Marisha on 19/02/2018.
 */
public abstract class AbstractArrayStorage implements Storage {

    protected static final int STORAGE_LIMIT = 10000;
    protected int size = 0;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

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
        insertElement(r, index);
        size++;
    }

    public void update(Resume r){
        int index = getIndex(r.getUuid());
        if (index < 0){
            System.out.println("No such resume!!!");
            return;
        }
        storage[index] = r;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0){
            System.out.println("No such resume!!!");
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0){
            System.out.println("No such resume!!!");
            return;
        }
        deleteElement(index);
        storage[size-1] = null;
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int getSize() {
        return size;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insertElement(Resume r, int index);

    protected abstract void deleteElement(int index);

}
