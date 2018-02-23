package storage;

import exception.ResumeExistsStorageException;
import exception.ResumeNotExistsStorageException;
import exception.StorageException;
import model.Resume;

import java.util.Arrays;

/**
 * Created by Marisha on 19/02/2018.
 */
public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_LIMIT = 10000;
    protected int size = 0;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void doSave(Resume r, Object index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Can't save resume: " + r.getUuid() + ". Storage overflow.", r.getUuid());
        }
        insertElement(r, (Integer) index);
        size++;
    }

    @Override
    protected boolean exists(Object searchKey) {
        return (Integer) searchKey >= 0;
    }

    @Override
    protected void doUpdate(Resume r, Object index) {
        storage[(Integer) index] = r;
    }

    @Override
    protected Resume doGet(Object index) {
        return storage[(Integer) index];
    }

    @Override
    protected void doDelete(Object index) {
        deleteElement((Integer) index);
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

    protected abstract void insertElement(Resume r, int index);

    protected abstract void deleteElement(int index);

    protected abstract Integer getSearchKey(String uuid);

}
