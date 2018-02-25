package storage;

import exception.StorageException;
import model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Marisha on 19/02/2018.
 */
public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {

    protected static final int STORAGE_LIMIT = 10000;
    protected int size = 0;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void doSave(Resume r, Integer index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Can't save resume: " + r.getUuid() + ". Storage overflow.", r.getUuid());
        }
        insertElement(r, index);
        size++;
    }

    @Override
    protected boolean exists(Integer searchKey) {
        return searchKey >= 0;
    }

    @Override
    protected void doUpdate(Resume r, Integer index) {
        storage[index] = r;
    }

    @Override
    protected Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    protected void doDelete(Integer index) {
        deleteElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    public int getSize() {
        return size;
    }

    protected abstract void insertElement(Resume r, int index);

    protected abstract void deleteElement(int index);

    protected abstract Integer getSearchKey(String uuid);

}
